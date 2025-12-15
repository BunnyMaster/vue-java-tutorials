package com.redis.redis.helper;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HotelHelper {

    private final RedisTemplate<String, Object> redisTemplate;

    public HotelHelper(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param key 锁的key
     */
    public boolean tryLock(String key) {
        // 不存在返回Ture
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtils.isTrue(flag);
    }

    /**
     * 释放锁
     *
     * @param key 锁的key
     */
    public void unlock(String key) {
        redisTemplate.delete(key);
    }

}
