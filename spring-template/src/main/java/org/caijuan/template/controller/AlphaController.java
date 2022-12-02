package org.caijuan.template.controller;

import org.caijuan.template.aop.AccessHandler;
import org.caijuan.template.aop.Audit;
import org.caijuan.template.domain.bean.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alpha")
public class AlphaController {

    @Audit(returnHandler = AccessHandler.class)
    @PostMapping("/user/save")
    public String save(@RequestBody User user) {
        return "success";
    }
}
