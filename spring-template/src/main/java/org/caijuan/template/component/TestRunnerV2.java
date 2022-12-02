package org.caijuan.template.component;

import lombok.extern.slf4j.Slf4j;
import org.caijuan.template.service.LoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Slf4j
@Component
public class TestRunnerV2 implements CommandLineRunner {

    @Autowired
    private LoadDataService loadDataService;

    @Override
    public void run(String... args) {
        log.info("CommandLineRunner > TestRunner#run...");
        loadDataService.load();
    }
}