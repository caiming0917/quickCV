package org.caijuan.template.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.caijuan.template.aop.Audit;
import org.caijuan.template.aop.CustomReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {


    public SpringBeanPostProcessor() {
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Audit.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), Audit.class.getCanonicalName());

        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            CustomReference rpcReference = declaredField.getAnnotation(CustomReference.class);
            if (rpcReference != null) {

                ClientProxy client = new ClientProxy();
                Object clientProxy = client.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }
}
