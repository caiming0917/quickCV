package org.caijuan.template.controller;

import org.caijuan.template.aop.AccessHandler;
import org.caijuan.template.aop.Audit;
import org.caijuan.template.domain.bean.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/alpha")
public class AlphaController {

    @Audit(returnHandler = AccessHandler.class)
    @RequestMapping("/user/save")
    public String save(@RequestBody User user) {
        return "success";
    }
}
