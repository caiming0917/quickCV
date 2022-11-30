package org.caijuan.template.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class AService {
    @PostConstruct
    public void init() {
        log.info("AService#PostConstruct init...");
    }
}