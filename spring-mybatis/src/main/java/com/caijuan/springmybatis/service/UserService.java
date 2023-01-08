package com.caijuan.springmybatis.service;

import com.caijuan.springmybatis.mapper.UserMapper;
import com.caijuan.springmybatis.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public int insert(User user){
        return userMapper.insert(user);
    }

    public User selectById(long userId){
        return userMapper.selectById(userId);
    }
}
