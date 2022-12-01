package com.caijuan.proxy.dynamicProxy.jdkDynamicProxy;


public class Main {
    public static void main(String[] args) {
        SmsService smsService = new SmsServiceImpl();
        SmsService smsServiceImplProxy = (SmsService) JdkProxyFactory.getProxy(smsService);
        smsServiceImplProxy.send("java");
    }
}
