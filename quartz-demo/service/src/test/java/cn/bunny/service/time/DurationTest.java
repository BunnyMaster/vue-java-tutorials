package cn.bunny.service.time;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DurationTest {
    // 当前时间
    @Test
    void testInstant() {
        Instant now = Instant.now();
        // 2024-07-29T14:53:01.965019Z
        System.out.println(now);
    }

    // 时间秒相加
    @Test
    void testDuration() {
        Instant now = Instant.now();

        // 在当前时间基础上加上1纳秒
        Instant plusNanos = now.plusNanos(1L);
        // 在当前时间基础上加上1毫秒
        Instant plusMillis = now.plusMillis(1L);
        // 在当前时间基础上再加上1秒
        Instant seconds = now.plusSeconds(1L);
        Duration between = Duration.between(now, seconds);
        // PT1S
        System.out.println(between);
        System.out.println(plusNanos);
        System.out.println(plusMillis);
    }

    @Test
    void testDurationTo() {
        Instant now = Instant.now();
        // 加上一天
        Instant end = now.plus(1L, ChronoUnit.DAYS);
        Duration between = Duration.between(now, end);

        // 将 Duration.between 相差时间 转成纳秒
        long nanos = between.toNanos();
        System.out.println(nanos);
        // 将 Duration.between 相差时间 转成毫秒
        long millis = between.toMillis();
        System.out.println(millis);
        // 将 Duration.between 相差时间 转成天数
        long days = between.toDays();
        System.out.println(days);
        // 将 Duration.between 相差时间 转成小时
        long hours = between.toHours();
        System.out.println(hours);


        // 判断是否为0
        boolean zero = between.isZero();
        System.out.println(zero);

        // 是否为负数
        boolean negative = between.isNegative();
        System.out.println(negative);
    }
}
