package org.caijuan.template.aop;


public interface AuditHandler {
    String handler(Object[] args, Object result);
}