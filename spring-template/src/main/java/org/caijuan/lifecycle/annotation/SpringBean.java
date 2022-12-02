package org.caijuan.lifecycle.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SpringBean implements InitializingBean, DisposableBean, ApplicationContextAware,
        BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, ApplicationRunner {

    @Autowired
    TestService testService;

	public SpringBean() {
		System.out.println("1、SpringBean构造方法:" + testService);
	}

    @Override
    public void setBeanName(String name) {
        System.out.println("2、setBeanName:" + testService);
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("3、setBeanClassLoader");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4、setBeanFactory");
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("6、afterPropertiesSet");
	}

    public void initMethod() {
        System.out.println("7、initMethod");
    }

	@Override
	public void destroy() throws Exception {
		System.out.println("9、destroy");
	}

	public void destroyMethod() {
		System.out.println("10、destroyMethod");
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("扩展1、调用ApplicationContext#setApplicationContext方法");
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("扩展2、postConstruct");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("扩展3、preDestroy");
    }

    // 依托 springboot
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("扩展4、调用ApplicationContext#setApplicationContext方法");
    }
}
