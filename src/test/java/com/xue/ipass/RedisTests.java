package com.xue.ipass;

import com.xue.ipass.dao.VideoMapper;
import com.xue.ipass.po.VideoPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest

/**redis缓存测试*/
public class RedisTests {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testqueryss() {

        /*Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
            System.out.println(key);
        }*/
        //redisTemplate.opsForValue().set("name","小黑");

        /*ValueOperations valueOperations = redisTemplate.opsForValue();
        Object name = valueOperations.get("name");
        System.out.println(name);*/

       /* ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("name","小黑");
        String name = stringStringValueOperations.get("name");
        System.out.println(name);*/

        //为值设置过期时间
        stringRedisTemplate.expire("name",1000, TimeUnit.MILLISECONDS);


        for (int i =0;i<20;i++){
            Boolean name = stringRedisTemplate.hasKey("name");

            System.out.println(name);
        }
    }
}
