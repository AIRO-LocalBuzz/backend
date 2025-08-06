package backend.airo.support;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClutrFatvlJobListener implements JobExecutionListener {

    private final TimeCatch timer = new TimeCatch("ClutrFatvlJob");

    @Override
    public void afterJob(JobExecution jobExecution) {
        timer.end();

        long totalRead = jobExecution.getStepExecutions()
                .stream()
                .mapToLong(StepExecution::getReadCount)
                .sum();

        long totalWritten = jobExecution.getStepExecutions()
                .stream()
                .mapToLong(StepExecution::getWriteCount)
                .sum();

        long totalSkipped = jobExecution.getStepExecutions()
                .stream()
                .mapToLong(StepExecution::getProcessSkipCount)
                .sum();

        long totalProcessed = totalRead - totalSkipped;

        log.info("총 처리 건수: {}", totalWritten);
        log.info("Read: {}, Processed: {}, Written: {}, Skipped: {}", totalRead, totalProcessed, totalWritten, totalSkipped);
    }
}