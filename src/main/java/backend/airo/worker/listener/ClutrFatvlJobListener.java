package backend.airo.worker.listener;

import backend.airo.cache.clutr_fatvl.ClutrFatvlCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClutrFatvlJobListener implements JobExecutionListener {


    private final ClutrFatvlCacheService clutrFatvlCacheService;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            clutrFatvlCacheService.evictClutrFatvl();
            log.info("Redis ClutrFatvl All Data Evict");
        }
    }
}