package com.caijuan.juc.limiter;

/**
 * @author cai juan
 * @date 2023/2/12 22:34
 */
public interface Limiter {

    <T> boolean tryAcquire(Request<T> request);

    <T> void handleRequest(Request<T> request);
}
