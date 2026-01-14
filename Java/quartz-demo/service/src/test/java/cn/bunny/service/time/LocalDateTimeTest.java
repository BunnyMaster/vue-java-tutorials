package cn.bunny.service.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest {
    //
    @Test
    void testLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();

        // 当前时间 2024-07-29T23:30:48.948003200
        System.out.println(now);
    }

    @Test
    void testDateTImeFormatter() {
        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 待解析的日期时间字符串
        String dateTimeString = "2024-07-30 15:30:00";

        // 解析日期时间字符串为 LocalDate 对象
        LocalDate parsedDate = LocalDate.parse(dateTimeString, formatter);

        // 解析时间
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        System.out.println("有时间包含T：" + localDateTime);// 2024-07-30T15:30 会有 T 在里面
        System.out.println("不包含T：" + localDateTime.format(formatter));

        // 输出解析后的 LocalDate
        System.out.println("Parsed LocalDate: " + parsedDate);
    }
}
