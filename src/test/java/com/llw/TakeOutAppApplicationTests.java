package com.llw;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
class TakeOutAppApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * Jedis操作Redis
     */
    @Test
    public void testRedis() {
        //1.获取连接
        Jedis jedis = new Jedis("localhost", 6379);

        //2.执行具体操作
        jedis.set("username", "zhangsan");

        String username = jedis.get("username");
        System.out.println(username);

//        jedis.del("username");

        jedis.hset("myhash", "addr", "beijing");

        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        //3.关闭连接
        jedis.close();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型数据
     */
    @Test
    public void testRedisString() {
        redisTemplate.opsForValue().set("city2", "beijing");

        String city = (String) redisTemplate.opsForValue().get("city2");
        System.out.println(city);
    }

    /**
     * 操作Hash类型数据
     */
    @Test
    public void testRedisHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("myhash2", "name", "wangwu");
        hashOperations.put("myhash2", "addr", "zhejiang");

        String name = (String) hashOperations.get("myhash2", "name");

        Set keys = hashOperations.keys("myhash2");

        keys.forEach(System.out::println);

        List values = hashOperations.values("myhash2");

        values.forEach(System.out::println);
    }

    /**
     * 操作List数据
     */
    @Test
    public void testRedisList() {
        ListOperations listOperations = redisTemplate.opsForList();
        redisTemplate.delete("mylist");
        listOperations.leftPush("mylist", "a");
        listOperations.leftPushAll("mylist", "b", "c", "d");

        List mylist = listOperations.range("mylist", 0, -1);
        mylist.forEach(System.out::print);
        System.out.println("");

        listOperations.rightPop("mylist");

        List mylist1 = listOperations.range("mylist", 0, -1);
        mylist1.forEach(System.out::println);
    }

    @Test
    public void testCommon () {
        Set keys = redisTemplate.keys("*");
        keys.forEach(System.out::println);

        Boolean my = redisTemplate.hasKey("mylist");
        System.out.println(my);

        DataType myhash = redisTemplate.type("myhash2");
        System.out.println(myhash.name());
    }
}
