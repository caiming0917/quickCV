package org.caijuan.lifecycle.annotation;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public TestService(){
        System.out.println("TestService 构造函数");
    }
}
