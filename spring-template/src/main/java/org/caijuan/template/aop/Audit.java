package org.caijuan.template.aop;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Audit {
    // 正常返回的方法
    Class<? extends AuditHandler> returnHandler() default DefaultHandler.class;

    // 抛出异常的方法
    Class<? extends AuditHandler> exceptionHandler() default DefaultHandler.class;
}