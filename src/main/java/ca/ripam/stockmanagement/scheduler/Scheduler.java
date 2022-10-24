package ca.ripam.stockmanagement.scheduler;

import ca.ripam.stockmanagement.service.StockBulkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final StockBulkService bulkService;

    @Scheduled(cron = "5 * * * * *")
    public void cronJobSch() {
        log.info("Running cron job");

        bulkService.retrieveNextFiles(10)
                .stream()
                .parallel()
                .forEach(bulkService::processFile);
    }
}
