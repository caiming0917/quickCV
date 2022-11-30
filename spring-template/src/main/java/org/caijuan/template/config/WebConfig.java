package org.caijuan.template.config;

import org.caijuan.template.interceptor.AuthInterceptor;
import org.caijuan.template.utils.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，
 * 可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter。
 *
 * https://blog.csdn.net/zhangpower1993/article/details/89016503
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Bean
    public AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器 和 拦截路径
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 添加数据转换器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }
}