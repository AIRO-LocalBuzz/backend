package backend.airo.batch.cure_fatvl;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.infra.open_api.clure_fatvl.client.OpenApiClureFatvlFeignClient;
import backend.airo.infra.open_api.clure_fatvl.dto.OpenApiClureFatvlResponse;
import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncClutrFatvlDataCollector {

    private final OpenApiClureFatvlFeignClient openApiClureFatvlFeignClient;
    private static final DateTimeFormatter DASHED = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int PAGE_SIZE = 1000;

    @Async("apiTaskExecutor")
    public CompletableFuture<List<ClutrFatvl>> fetchAllByStartDateAsync(LocalDate startDate) {
        try {
            List<ClutrFatvlInfo> infos = fetchAllByStartDate(startDate);
            List<ClutrFatvl> entities = infos.stream()
                    .map(ClutrFatvlEntity::toDomain)
                    .collect(Collectors.toList());

            return CompletableFuture.completedFuture(entities);

        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }

    private List<ClutrFatvlInfo> fetchAllByStartDate(LocalDate startDate) {
        int page = 1;
        List<ClutrFatvlInfo> acc = new ArrayList<>();
        while (true) {
            try {
                OpenApiClureFatvlResponse<ClutrFatvlInfo> res =
                        openApiClureFatvlFeignClient.getClutrFatvlInfo(
                                String.valueOf(page),
                                String.valueOf(PAGE_SIZE),
                                startDate.format(DASHED)
                        );

                var response = res.response();
                if (response == null || response.header() == null || response.body() == null) {
                    break;
                }

                var body = response.body();
                List<ClutrFatvlInfo> items = body.items();
                if (items == null || items.isEmpty()) break;

                acc.addAll(items);

                int total = Integer.parseInt(body.totalCount());
                if (page * PAGE_SIZE >= total) break;

                page++;

            } catch (Exception e) {
                break;
            }
        }

        return acc;
    }
}
