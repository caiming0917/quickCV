package org.caijuan.template.domain.bean;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String name;
    private Date registerDate;

    public void setUserName(String name) {
        this.name = name;
    }
}