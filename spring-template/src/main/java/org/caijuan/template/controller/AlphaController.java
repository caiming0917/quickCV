package org.caijuan.template.controller;

import org.caijuan.template.aop.AccessHandler;
import org.caijuan.template.aop.Audit;
import org.caijuan.template.aysnc.AsyncService;
import org.caijuan.template.aysnc.ScheduledService;
import org.caijuan.template.domain.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ScheduledService scheduledService;

    @Audit(returnHandler = AccessHandler.class)
    @RequestMapping("/user/save")
    public String save(@RequestBody User user) {
        asyncService.asyncMethod();
        scheduledService.scheduledMethod();
        return "success";
    }
}
