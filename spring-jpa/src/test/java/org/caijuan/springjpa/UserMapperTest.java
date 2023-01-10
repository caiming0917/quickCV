package org.caijuan.springjpa;// UserRepository01.java

import org.caijuan.springjpa.dao.UserDO;
import org.caijuan.springjpa.dao.UserRepository01;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringJpaApplication.class)
public class UserMapperTest {

    @Autowired
    private UserRepository01 userMapper;

    @Test
    public void testInsert() {
        UserDO user = new UserDO().setId(1).setUsername(UUID.randomUUID().toString())
                .setPassword("nicai").setCreateTime(new Date());
        userMapper.save(user);
    }


    @Test
    public void testSelectById() {
        Optional<UserDO> optional = userMapper.findById(1);
        System.out.println(optional.isPresent() ? optional.get() : "NULL");
    }

/*    @Test
    public void testSelectByUsername() {
        userMapper.selectByUsername("yunai");
    }*/

    @Test
    public void testSelectByIds() {
        Iterable<UserDO> userDOS = userMapper.findAllById(Arrays.asList(1, 3));
        List<UserDO> users = new ArrayList<>();
        userDOS.forEach((users::add));
        System.out.println("users：" + users.size());
    }

    @Test
    public void testDeleteById() {
        userMapper.deleteById(1);
    }

}