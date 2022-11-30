package org.caijuan.template.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DService implements DisposableBean {
 
    @Override
    public void destroy() throws Exception {
        log.info("DisposableBean > DService#destroy...");
    }
}