package org.caijuan.template.component;

import lombok.extern.slf4j.Slf4j;
import org.caijuan.template.domain.bean.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof User) {
            log.info("BeanPostProcessor > MyBeanPostProcessor#postProcessAfterInitialization...");
            ((User) bean).setUserName("张三");
        }
        return bean;
    }
}