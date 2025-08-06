package backend.airo.worker.batch;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class ClutrFatvlSchedule {

    private final JobLauncher launcher;
    private final JobExplorer jobExplorer;

    // 필요한 Job만 주입
    private final Job clutrFatvlJob;

    // 매일 00:05(KST) 실행
//    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    @PostConstruct
    public void runDaily() throws Exception {
        // 중복 실행 방지 (이미 돌고 있으면 스킵)
        if (!jobExplorer.findRunningJobExecutions("ClutrFatvlJob").isEmpty()) return;

        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("pageSize", "1000")
                .toJobParameters();

        launcher.run(clutrFatvlJob, params);
    }


}
