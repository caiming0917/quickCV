package com.caijuan.juc.limiter;

import java.time.LocalTime;

/**
 * @author cai juan
 * @date 2023/2/12 22:34
 */
public interface Limiter {

    <T> boolean tryAcquire(Request<T> request);

    default <T> void handleRequest(Request<T> request) {
        request.setHandleTime(LocalTime.now());
        // SmallTool.info("requestID => " + request.getRequestId());
    }
}
