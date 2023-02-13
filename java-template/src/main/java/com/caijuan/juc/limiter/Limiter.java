package com.caijuan.juc.limiter;

import com.caijuan.utils.SmallTool;

/**
 * @author cai juan
 * @date 2023/2/12 22:34
 */
public interface Limiter {

    <T> boolean tryAcquire(Request<T> request);

    default <T> void handleRequest(Request<T> request) {
        SmallTool.info("requestID => " + request.getRequestId());
    }
}
