package org.caijuan.template.aysnc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduledService {

    @Async()
    @Scheduled(cron = "*/5 * * * * *")
    public void scheduledMethod() {
        // 定时任务的方法逻辑
        log.info("Scheduled ...");
    }

    @Async()
    @Scheduled(cron = "*/5 * * * * *")
    public void scheduledMethod2() {
        // 定时任务的方法逻辑
        log.info("Scheduled ...");
    }
}