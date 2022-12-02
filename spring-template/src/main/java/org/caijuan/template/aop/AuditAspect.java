package org.caijuan.template.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    // 基于目录的切面
//    @Pointcut("execution(* org.caijuan.template.controller.*.*(..))")
    // 基于注解的切面
    @Pointcut("@annotation(org.caijuan.template.aop.Audit)")
    public void pointcut() {
//        切点可以写代码，但不推荐
//        log.debug("进入切入点，开始构造日志...");
    }

    @AfterReturning(value = "pointcut() && @annotation(annotation)", returning = "result")
    public void changeState(JoinPoint joinPoint, Object result, Audit annotation) throws IllegalAccessException, InstantiationException {
        AuditHandler auditHandler = annotation.returnHandler().newInstance();
        String message = auditHandler.handler(joinPoint.getArgs(), result);
        log.info("AuditAspect AfterReturning#changeState => {}", message);
    }

    @AfterThrowing(value = "pointcut() && @annotation(annotation)")
    public void changeState(JoinPoint joinPoint, Audit annotation) throws IllegalAccessException, InstantiationException {
        AuditHandler auditHandler = annotation.returnHandler().newInstance();
        String message = auditHandler.handler(joinPoint.getArgs(), null);
        log.info("AuditAspect AfterThrowing#changeState => {}", message);
    }

}