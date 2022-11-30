package org.caijuan.template.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义拦截器，统一处理token鉴权验证问题
 */
@Slf4j
@Component
@Order(value = 1)
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        return checkAuth(token);
    }

    private boolean checkAuth(String token) {
        log.info("权限校验: {}", token);
        return true;
    }
}
