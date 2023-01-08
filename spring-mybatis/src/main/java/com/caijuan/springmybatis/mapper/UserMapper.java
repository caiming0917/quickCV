package com.caijuan.springmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caijuan.springmybatis.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
