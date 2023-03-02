package org.caijuan.template.aysnc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {

    @Async
    public void asyncMethod() {
        // 异步执行的方法逻辑
        log.info("async");
    }
}