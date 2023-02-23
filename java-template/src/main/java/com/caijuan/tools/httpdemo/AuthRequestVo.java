package com.caijuan.tools.httpdemo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequestVo {
    private String name;
    private String passwd;
}