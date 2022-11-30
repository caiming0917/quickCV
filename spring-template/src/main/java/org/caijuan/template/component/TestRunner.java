package org.caijuan.template.component;

import lombok.extern.slf4j.Slf4j;
import org.caijuan.template.service.LoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Priority 和 order 的区别
 * https://blog.csdn.net/qq_37130607/article/details/113823971
 */
//@Priority(1)
@Slf4j
@Order(2)   // 指定执行顺序
@Component
public class TestRunner implements ApplicationRunner {

    @Autowired
    private LoadDataService loadDataService;

    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationRunner > TestRunner#run...");
        loadDataService.load();
    }
}