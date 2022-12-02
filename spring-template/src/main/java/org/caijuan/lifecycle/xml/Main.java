package org.caijuan.lifecycle.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * https://juejin.cn/post/7075168883744718856
 */
public class Main {


    public static void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        PersonBean personBean = (PersonBean) context.getBean("personBean");
        personBean.work();
        ((ClassPathXmlApplicationContext) context).close();
    }


    public static void testV2(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        PersonBeanV2 personBean = (PersonBeanV2) context.getBean("personBeanV2");
        personBean.work();
//        ((ClassPathXmlApplicationContext) context).close();
    }

    public static void main(String[] args) {
//        test();
        testV2();
    }
}
