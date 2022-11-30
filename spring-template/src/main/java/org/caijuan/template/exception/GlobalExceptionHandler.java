package org.caijuan.template.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：
 * step 1 : @RestControllerAdvice
 * step 2 : @ExceptionHandler(Exception.class)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 指定异常类型
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("发生异常 ： {}",e.getMessage());
        e.printStackTrace();
        if (e instanceof ArithmeticException) {
            return "数据异常";
        }
        if (e instanceof RuntimeException) {
            return "服务器内部异常";
        }
        return "未知异常";
    }
}