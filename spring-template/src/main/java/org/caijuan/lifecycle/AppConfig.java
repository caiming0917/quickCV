package org.caijuan.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"org.caijuan.lifecycle"})
public class AppConfig {
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public SpringBean springBean() {
        return new SpringBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);
        annotationConfigApplicationContext.close();
    }
}