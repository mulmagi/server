package shop.mulmagi.app.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@Component
@RequiredArgsConstructor
public class OverdueRentalBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job overdueRentalBatchJob;
    private final Job resetUserExperienceBatchJob;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runOverdueRentalBatchJob() {
        try {
            jobLauncher.run(overdueRentalBatchJob, new JobParameters());
        } catch (Exception e) {
        }
    }

    @Scheduled(cron = "0 0 0 1 1,7 ?")
    public void runResetUserExperienceBatchJob() {
        try {
            jobLauncher.run(resetUserExperienceBatchJob, new JobParameters());
        } catch (Exception e) {
        }
    }
}
