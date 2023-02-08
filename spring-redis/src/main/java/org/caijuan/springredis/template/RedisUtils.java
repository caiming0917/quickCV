package org.caijuan.springredis.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate redisTemplate;

    public Boolean addZset(String key, Object value, double seqNo) {
        try {
            return redisTemplate.opsForZSet().add(key, value, seqNo);
        } catch (Exception e) {
            log.error("[RedisUtils.addZset] [error]", e);
            return false;
        }
    }

    public Long countZset(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("[RedisUtils.countZset] [error] [key is {}]", key, e);
            return 0L;
        }
    }


    public Set<Object> rangeZset(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("[RedisUtils.rangeZset] [error] [key is {},start is {},end is {}]", key, start, end, e);
            return null;
        }
    }

    /**
     * 根据key和value移除指定元素
     */
    public Long removeZset(String key, Object value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 获取对应key和value的score
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }


    /**
     * 指定范围内元素排序
     */
    public Set<Object> rangeByScore(String key, double v1, double v2) {
        return redisTemplate.opsForZSet().rangeByScore(key, v1, v2);
    }


    /**
     * 指定元素增加指定值
     */
    public Object addScore(String key, Object obj, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, obj, score);
    }

    public Object rank(String key, Object obj) {
        return redisTemplate.opsForZSet().rank(key, obj);
    }


    public void test01() {
        RedisUtils redisUtils = new RedisUtils();
        // 初始化person集合
        List<Person> personList = Arrays.asList(new Person(1287989870372388866L, 29),
                new Person(1287989874193399810L, 18), new Person(1287989878043770883L, 38),
                new Person(1287989866203250690L, 12), new Person(1287989956804411394L, 45));
        for (int i = 0; i < personList.size(); i++) {
            // 新增，key为person，value为uuid唯一标识，score为年龄
            System.out.println(addZset("person", personList.get(i).getUuid(), personList.get(i).getAge()));
        }


        // 查询key为person的集合个数
        Long count = redisUtils.countZset("person");
        System.out.println(count);

        // 查询所有
        System.out.println(redisUtils.rangeZset("person", 0, -1));
        // 查询前3个
        System.out.println(redisUtils.rangeZset("person", 0, 2));


        // 删除指定key和value对应的元素（存在的）
        System.out.println(redisUtils.removeZset("person", 1287989874193399810L));
        // 删除指定key和value对应的元素（不存在的）
        System.out.println(redisUtils.removeZset("111", 1287989874193399810L));
        // 查询删除后的所有元素
        System.out.println(redisUtils.rangeZset("person", 0, -1));
        System.out.println(redisUtils.rangeByScore("person", 10, 30));


        // 修改score前查询score值
        System.out.println(redisUtils.score("person", 1287989956804411394L));
        // score+5，返回修改后的score
        System.out.println(redisUtils.addScore("person", 1287989956804411394L, 5));


        System.out.println(redisUtils.rank("person", 1287989866203250690L));
    }
}
