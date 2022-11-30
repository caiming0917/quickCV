package org.caijuan.template.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Slf4j
@Scope("threadLocalScope")
@Service
public class CService {
    public void add() {
        log.info("threadLocalScope > CService#add...");
    }
}