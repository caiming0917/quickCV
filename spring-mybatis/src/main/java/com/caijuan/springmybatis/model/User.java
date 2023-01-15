package com.caijuan.springmybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "public.user", autoResultMap = true)
public class User {

    @TableId(type = IdType.AUTO)
    @TableField(value ="id")
    private Long id;

    @TableField(value ="user_name")
    private String userName;

    @TableField(value ="age")
    private Integer age;

    @TableField(value ="email")
    private String email;

    @TableField(value ="monitor_notice",typeHandler = com.caijuan.springmybatis.mapper.handler.JSONTypeHandlerPg.class)
    private Object monitorNotice;
}