package com.caijuan.springmybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caijuan.springmybatis.mapper.UserMapper;
import com.caijuan.springmybatis.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    public List<User> test(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.and(wrapper -> wrapper.eq("email","123@qq.com")
                .or().eq("user_name","张三"));

        queryWrapper.eq("age",22);

        return userMapper.selectList(queryWrapper);
    }
}
