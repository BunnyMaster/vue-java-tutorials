package com.redis.redis;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

@SpringBootTest
public class RedisDemoApplicationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString() {
        redisTemplate.opsForValue().set("name", "球球");
        Object name = redisTemplate.opsForValue().get("name");

        System.out.println("name=" + name);
    }

    @Test
    void testJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "球球");
        jsonObject.put("age", LocalDateTime.now());

        redisTemplate.opsForValue().set("json", jsonObject.toString());
        Object object = redisTemplate.opsForValue().get("json");

        System.out.println(object);
    }

}
