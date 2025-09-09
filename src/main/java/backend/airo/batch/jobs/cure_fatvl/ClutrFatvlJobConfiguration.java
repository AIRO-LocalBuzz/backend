package backend.airo.batch.jobs.cure_fatvl;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvl;
import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlResponse;
import backend.airo.cache.area_code.AreaCodeCacheService;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.persistence.clutrfatvl.repository.ClutrFatvlBulkRepository;
import backend.airo.worker.listener.ClutrFatvlJobListener;
import backend.airo.worker.listener.NotificationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ClutrFatvlJobConfiguration {

    private static final String JOB_NAME = "ClutrFatvlJob";
    private static final String CONTENT_ID = "15";

    private final JobRepository repo;
    private final PlatformTransactionManager tx;
    private final ClutrFatvlFetcher clutrFatvlFetcher;
    private final ClutrFatvlBulkRepository clutrFatvlBulkRepository;

    // === Job ===
    @Bean(name = JOB_NAME)
    public Job clutrFatvlJob(Step clutrFatvlStep, ClutrFatvlJobListener listener, NotificationListener notificationListener) {
        return new JobBuilder(JOB_NAME, repo)
                .start(clutrFatvlStep)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(notificationListener)
                .build();
    }

    // === step ===
    @Bean(name = JOB_NAME + ".step")
    public Step clutrFatvlStep(
            ItemStreamReader<List<OpenApiClutrFatvl>> reader,
            ItemProcessor<List<OpenApiClutrFatvl>, List<ClutrFatvl>> processor,
            ItemWriter<List<ClutrFatvl>> writer,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int chunkSize
    ) {
        return new StepBuilder(JOB_NAME + ".step", repo)
                .<List<OpenApiClutrFatvl>, List<ClutrFatvl>>chunk(chunkSize, tx) // 한 번에 10개의 List (최대 10,000건)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    // === Reader ===
    @Bean(name = JOB_NAME + ".reader")
    @StepScope
    public ItemStreamReader<List<OpenApiClutrFatvl>> clutrReader(
            @Value("#{jobParameters['pageSize'] ?: 1000}") Integer pageSize) {

        return new ItemStreamReader<>() {
            private final LocalDate getNowDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
            private final DateTimeFormatter YMD = DateTimeFormatter.BASIC_ISO_DATE;

            private LocalDate current = getNowDate.minusMonths(6);
            private final LocalDate end = getNowDate;
            private boolean finished = false;
            private int page = 1;

            @Override
            public List<OpenApiClutrFatvl> read() {
                if (finished) return null;

                List<OpenApiClutrFatvl> buffer = new ArrayList<>();
                while (buffer.size() < 1000 && !finished) {
                    if (current.isAfter(end)) {
                        finished = true;
                        break;
                    }

                    OpenApiClutrFatvlResponse resp = clutrFatvlFetcher.fetchClutrFatvl(
                            String.valueOf(page),
                            CONTENT_ID,
                            current.format(YMD)
                    );

                    if (resp.resultCode() == null || !"0000".equals(resp.resultCode())) {
                        current = current.plusDays(1);
                        page = 1;
                        continue;
                    }

                    List<OpenApiClutrFatvl> items = resp.openApiClutrFatvls();
                    if (items == null || items.isEmpty()) {
                        current = current.plusDays(1);
                        page = 1;
                        continue;
                    }

                    buffer.addAll(items);

                    if (items.size() < pageSize) {
                        current = current.plusDays(1);
                        page = 1;
                    } else {
                        page++;
                    }
                }

                return buffer.isEmpty() ? null : buffer;
            }

            @Override public void open(ExecutionContext executionContext) {}
            @Override public void update(ExecutionContext executionContext) {}
            @Override public void close() {}
        };
    }



    // === Processor ===
    @Bean(name = JOB_NAME + ".processor")
    public ItemProcessor<List<OpenApiClutrFatvl>, List<ClutrFatvl>> clutrProcessor() {
        return items -> items.stream()
                .map(item -> {
                    try {
                        return ClutrFatvl.create(item);
                    } catch (Exception e) {
                        log.error("Processor 예외 발생: {}", e.getMessage(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Bean(name = JOB_NAME + ".writer")
    public ItemWriter<List<ClutrFatvl>> clutrWriter() {
        return chunk -> {
            for (List<ClutrFatvl> group : chunk.getItems()) {
                if (!group.isEmpty()) {
                    clutrFatvlBulkRepository.batchInsert(group);
                    log.info("[Writer] batch insert size: {}", group.size());
                }
            }
        };
    }
}
