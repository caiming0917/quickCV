package org.caijuan.template.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessHandler implements SendAuditHandler {

    @Override
    public String handler(Object[] args, Object result) {
        log.info("result => {}",result);
        return "完成审计日志";
    }
}