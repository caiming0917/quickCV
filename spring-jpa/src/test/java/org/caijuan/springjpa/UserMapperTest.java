package org.caijuan.springjpa;

import org.caijuan.springjpa.dao.UserDO;
//import org.caijuan.springjpa.dao.UserRepository;
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
//    private UserRepository userRepository;
    private UserRepository01 userRepository;

    @Test
    public void testInsert() {
        List<Long> ids = new ArrayList<>();
        ids.add(10L);
        ids.add(13L);
        ids.add(14L);
        UserDO user = new UserDO().setId(1).setUsername(UUID.randomUUID().toString())
                .setPassword("nicai").setCreateTime(new Date()).setIds(ids);
        userRepository.save(user);
    }


    @Test
    public void testSelectById() {
        Optional<UserDO> optional = userRepository.findById(1);
        System.out.println(optional.isPresent() ? optional.get() : "NULL");
    }

/*    @Test
    public void testSelectByUsername() {
        userRepository.selectByUsername("yunai");
    }*/

    @Test
    public void testSelectByIds() {
        Iterable<UserDO> userDOS = userRepository.findAllById(Arrays.asList(1, 3));
        List<UserDO> users = new ArrayList<>();
        userDOS.forEach((users::add));
        System.out.println("users：" + users.size());
    }

    @Test
    public void testDeleteById() {
        userRepository.deleteById(1);
    }

}