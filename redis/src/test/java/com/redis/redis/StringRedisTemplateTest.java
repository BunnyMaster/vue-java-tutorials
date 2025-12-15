package com.redis.redis;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class StringRedisTemplateTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testString() {
        stringRedisTemplate.opsForValue().set("name", "球球");
        String name = stringRedisTemplate.opsForValue().get("name");

        System.out.println(name);
    }

    @Test
    void testMap() throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();

        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        SecurityProperties.User user = new SecurityProperties.User();
        user.setName("球球");
        user.setPassword(format);
        user.setRoles(List.of("admin", "a"));

        String userString = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user", userString);

        String userObject = stringRedisTemplate.opsForValue().get("user");
        SecurityProperties.User value = mapper.readValue(userObject, SecurityProperties.User.class);
        
        System.out.println(JSON.toJSONString(value));
    }

}
