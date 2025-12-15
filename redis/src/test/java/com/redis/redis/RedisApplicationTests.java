// package com.redis.redis;
//
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import redis.clients.jedis.Jedis;
//
// import java.util.Map;
//
// @SpringBootTest
// class RedisApplicationTests {
//
//     private Jedis jedis;
//
//     @BeforeEach
//     void setUp() {
//         jedis = new Jedis("192.168.2.19", 6379);
//         jedis.auth("Dev1234!");
//         jedis.select(0);
//     }
//
//     @AfterEach
//     void tearDown() {
//         jedis.close();
//     }
//
//     @Test
//     void contextLoads() {
//         String result = jedis.set("name", "dev");
//         System.out.println(result);
//
//         String name = jedis.get("name");
//         System.out.println(name);
//     }
//
//     @Test
//     void testHash() {
//         jedis.hset("user:1", "name", "dev");
//         jedis.hset("user:1", "name2", "dev2");
//
//         Map<String, String> map = jedis.hgetAll("user:1");
//         System.out.println(map);
//     }
//
// }
