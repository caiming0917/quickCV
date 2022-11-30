package org.caijuan.template.aop;


public interface SendAuditHandler {
    String handler(Object[] args, Object result);
}