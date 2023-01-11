package org.caijuan.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext appContext;

    // 继承了接口就会自动注入
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (appContext == null) {
            appContext = applicationContext;
        }
    }


    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getContext() {
        if (appContext == null)
            throw new IllegalStateException("applicaitonContext not been injected,please declare 'AppContext' in file:applicationContext.xml");
        return appContext;
    }

    /**
     * 通过name获取 Bean.
     */
    public static Object getBean(String name) {
        return appContext.getBean(name);
    }

    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return appContext.getBean(name, clazz);
    }
}