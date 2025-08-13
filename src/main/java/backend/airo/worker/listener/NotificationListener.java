package backend.airo.worker.listener;

import backend.airo.support.notification.ServerStartupNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener implements JobExecutionListener {

    private final ServerStartupNotifier serverStartupNotifier;
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            long totalRead  = jobExecution.getStepExecutions().stream()
                    .mapToLong(StepExecution::getReadCount).sum();
            long totalWrite = jobExecution.getStepExecutions().stream()
                    .mapToLong(StepExecution::getWriteCount).sum();
            long totalSkip  = jobExecution.getStepExecutions().stream()
                    .mapToLong(se -> se.getSkipCount() + se.getReadSkipCount()
                            + se.getProcessSkipCount() + se.getWriteSkipCount())
                    .sum();

            LocalDateTime started  = jobExecution.getStartTime();
            LocalDateTime finished = jobExecution.getEndTime();
            Duration duration = (started != null && finished != null)
                    ? Duration.between(started, finished)
                    : Duration.ZERO;

            long tookSec = duration.getSeconds();
            long tookMs  = duration.toMillis();

            LocalDate startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
            LocalDate endDate   = startDate.plusMonths(6);

            serverStartupNotifier.collectClutrFatvlDataSuccessWithNotification(
                    totalWrite, totalRead, totalSkip, startDate, endDate, tookSec
            );

            log.info("ClutrFatvl batch completed. read={}, write={}, skip={}, took={}s ({} ms)",
                    totalRead, totalWrite, totalSkip, tookSec, tookMs);

        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            String reason = jobExecution.getAllFailureExceptions().stream()
                    .findFirst().map(Throwable::getMessage).orElse("Unknown");
            serverStartupNotifier.collectClutrFatvlDataFailWithNotification(reason);
            log.error("ClutrFatvl batch failed: {}", reason);
        }
    }
}
