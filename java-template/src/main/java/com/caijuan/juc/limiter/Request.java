package com.caijuan.juc.limiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@Data
@AllArgsConstructor
public class Request<T> {

    private final static AtomicLong no = new AtomicLong();
    private long requestId;

    private LocalTime handleTime;

    private LocalTime launchTime;

    private T data;

    public Request() {
        this.requestId = no.getAndIncrement();
        this.launchTime = LocalTime.now();
    }
}
