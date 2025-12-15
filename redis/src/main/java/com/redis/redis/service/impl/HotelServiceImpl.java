package com.redis.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.redis.helper.HotelHelper;
import com.redis.redis.mapper.HotelMapper;
import com.redis.redis.model.entity.HotelEntity;
import com.redis.redis.model.vo.HotelVO;
import com.redis.redis.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class HotelServiceImpl extends ServiceImpl<HotelMapper, HotelEntity> implements HotelService {

    private static final String NULL_VALUE = "NULL";

    private final HotelHelper hotelHelper;

    private final RedisTemplate<String, Object> redisTemplate;

    public HotelServiceImpl(HotelHelper hotelHelper, RedisTemplate<String, Object> redisTemplate) {
        this.hotelHelper = hotelHelper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据Id查询酒店信息
     *
     * @param id 酒店 Id
     * @return 酒店信息
     */
    @Override
    public HotelVO queryWithMutex(Long id) {
        String redisKey = "hotel:" + id;
        String hotelLock = "lock:" + redisKey;

        // 1. 查询Redis缓存
        Object cacheData = redisTemplate.opsForValue().get(redisKey);

        if (cacheData != null) {
            if (NULL_VALUE.equals(cacheData.toString())) {
                // 空值缓存
                return null;
            }
            return JSONUtil.toBean(JSON.toJSONString(cacheData), HotelVO.class);
        }

        // 2. 尝试获取锁进行缓存重建
        boolean lockAcquired = false;
        try {
            // 尝试获取锁，最多重试3次
            for (int i = 0; i < 3; i++) {
                lockAcquired = hotelHelper.tryLock(hotelLock);
                if (lockAcquired) {
                    break;
                }
                // 短暂等待后重试
                Thread.sleep(50);
            }

            if (!lockAcquired) {
                // 获取锁失败，直接返回旧数据或null，避免等待
                return null;
            }

            // 3. 再次检查缓存（双重检查）
            cacheData = redisTemplate.opsForValue().get(redisKey);
            if (cacheData != null) {
                if (NULL_VALUE.equals(cacheData.toString())) {
                    return null;
                }
                return JSONUtil.toBean(JSON.toJSONString(cacheData), HotelVO.class);
            }

            // 4. 查询数据库
            HotelEntity hotel = super.getById(id);
            if (hotel == null) {
                // 缓存空值，防止缓存穿透
                redisTemplate.opsForValue().set(redisKey, NULL_VALUE, 5, TimeUnit.MINUTES);
                return null;
            }

            // 5. 构建VO并缓存
            HotelVO hotelVO = BeanUtil.copyProperties(hotel, HotelVO.class);
            hotelVO.setSystemTime(LocalDateTime.now());
            redisTemplate.opsForValue().set(redisKey, hotelVO, 30, TimeUnit.MINUTES);

            return hotelVO;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("查询被中断", e);
        } finally {
            hotelHelper.unlock(hotelLock);
        }
    }

    /**
     * 根据Id查询酒店信息
     *
     * @param id 酒店 Id
     * @return 酒店信息
     */
    @Override
    public HotelVO queryWithPassThrough(Long id) {
        // 缓存穿透

        String CACHE_HOTEL_KEY = "hotel:";
        String redisKey = CACHE_HOTEL_KEY + id;

        // 查询缓存
        String cacheValue = (String) redisTemplate.opsForValue().get(redisKey);

        if (cacheValue != null) {
            if (NULL_VALUE.equals(cacheValue)) {
                throw new RuntimeException("酒店不存在");
            }
            return JSON.parseObject(cacheValue, HotelVO.class);
        }

        // 查询数据库
        HotelEntity hotel = super.getById(id);
        if (hotel == null) {
            // 缓存空值，设置较短过期时间
            redisTemplate.opsForValue().set(redisKey, NULL_VALUE, 5, TimeUnit.MINUTES);
            throw new RuntimeException("酒店不存在");
        }

        HotelVO hotelVO = BeanUtil.copyProperties(hotel, HotelVO.class);
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(hotelVO), 30, TimeUnit.MINUTES);
        return hotelVO;

    }
}
