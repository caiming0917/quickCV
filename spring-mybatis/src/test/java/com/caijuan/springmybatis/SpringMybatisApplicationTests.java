package com.caijuan.springmybatis;

import com.alibaba.fastjson.JSONObject;
import com.caijuan.springmybatis.mapper.handler.JSONTypeHandlerPg;
import com.caijuan.springmybatis.model.MonitorNotice;
import com.caijuan.springmybatis.model.NoticeCategory;
import com.caijuan.springmybatis.model.User;
import com.caijuan.springmybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class SpringMybatisApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
//        test01();
        List<User> users = userService.test();
        System.out.println(users);
    }

    private void test01() {
        System.out.println(userService);

        User user = new User();
        user.setId(1015L);
        user.setAge(20);
        user.setUserName("张三");
        user.setEmail("123@qq.com");

        MonitorNotice monitorNotice = new MonitorNotice();
        HashMap<String, Boolean> map1 = new HashMap<>();
        map1.put("sms", true);
        map1.put("lanxin", false);
        NoticeCategory connectNotice = new NoticeCategory(true, map1);
        monitorNotice.setConnectNotice(connectNotice);

        HashMap<String, Boolean> map2 = new HashMap<>();
        map2.put("sms", true);
        map2.put("lanxin", false);
        NoticeCategory transNotice = new NoticeCategory(true, map2);
        monitorNotice.setTransNotice(transNotice);

        HashMap<String, Boolean> map3 = new HashMap<>();
        map3.put("sms", true);
        map3.put("lanxin", false);
        NoticeCategory exceptionNotice = new NoticeCategory(true, map3);
        monitorNotice.setExceptionNotice(exceptionNotice);

//        user.setMonitorNotice(JSONObject.toJSONString(monitorNotice));
        user.setMonitorNotice(monitorNotice);
        userService.insert(user);


        User user1 = userService.selectById(1015L);
        System.out.println(user1);
    }
}
