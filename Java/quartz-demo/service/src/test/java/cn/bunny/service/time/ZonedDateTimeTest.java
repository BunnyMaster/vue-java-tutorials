package cn.bunny.service.time;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;

public class ZonedDateTimeTest {
    @Test
    void testDateTime() {
        ZonedDateTime shanghai = ZonedDateTime.now();
        // 2024-07-30T09:07:17.131365900+08:00[Asia/Shanghai]
        System.out.println(shanghai);

        ZoneId zoneId = ZoneId.of("Europe/Paris");
        // 设置时区为巴黎
        ZonedDateTime paris = ZonedDateTime.now(zoneId);
        // 2024-07-30T07:46:52.456072600+02:00[Europe/Paris]
        System.out.println(paris);
    }

    @Test
    void testZoneDataTime() {
        ZoneId zoneId = ZoneId.of("Europe/Paris");

        // 2023-02-02T11:02:02.000000002+01:00[Europe/Paris]
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2023, 2, 2, 11, 2, 2, 2, zoneId);
        System.out.println(zonedDateTime);

        // 2024-07-30T12:12+02:00[Europe/Paris]
        ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(12, 12), zoneId);
        System.out.println(dateTime);

        // 加 3000 纳秒
        ZonedDateTime plussedNanos = dateTime.plusNanos(3000);
        System.out.println(plussedNanos);

        // 加 3600 秒
        ZonedDateTime plusSeconds = dateTime.plusSeconds(3600);
        System.out.println(plusSeconds);

        // 加3分钟
        ZonedDateTime plusMinutes = dateTime.plusMinutes(3);
        System.out.println(plusMinutes);

        // 加3个星期
        ZonedDateTime plussedWeeks = dateTime.plusWeeks(3L);
        System.out.println(plussedWeeks);

        // 在原基础上加 2 个星期
        ZonedDateTime plusWeeks = dateTime.plusWeeks(2);
        System.out.println(plusWeeks);

        // 加 3 个月
        ZonedDateTime plussedMonths = dateTime.plusMonths(3);
        System.out.println(plussedMonths);

        // 加 3 年
        ZonedDateTime plussedYears = dateTime.plusYears(3);
        System.out.println(plussedYears);
    }

    @Test
    void tesZonedDateTimeWith() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        // 将年份设置为2024年
        ZonedDateTime withedYear = zonedDateTime.withYear(2024);
        System.out.println(withedYear);

        // 将月份设置为6月
        ZonedDateTime withedMonth = zonedDateTime.withMonth(6);
        System.out.println(withedMonth);

        // 当前年的第200天
        ZonedDateTime withedDayOfYear = zonedDateTime.withDayOfYear(200);
        System.out.println(withedDayOfYear);

        // 当前月的第26天
        ZonedDateTime withedDayOfMonth = zonedDateTime.withDayOfMonth(26);
        System.out.println(withedDayOfMonth);

        // 当前天的 16 小时
        ZonedDateTime withedHour = zonedDateTime.withHour(16);
        System.out.println(withedHour);

        // 当前时间的 第16 分钟
        ZonedDateTime withedMinute = zonedDateTime.withMinute(16);
        System.out.println(withedMinute);

        // 设置当前秒为 16 秒
        ZonedDateTime withedSecond = zonedDateTime.withSecond(16);
        System.out.println(withedSecond);

        // 设置为1666纳秒
        ZonedDateTime withedNano = zonedDateTime.withNano(1666);
        System.out.println(withedNano);

        // 使用withZoneSameLocal()方法将时区调整为相同的本地时区，不改变时间
        ZonedDateTime sameLocal = zonedDateTime.withZoneSameLocal(ZoneId.of("America/New_York"));
        System.out.println("Same Local Zone: " + sameLocal);

        // 使用withZoneSameInstant()方法将时区调整为相同的即时时区，调整时间以保持相同的瞬时点，改变时区，改变时间
        ZonedDateTime sameInstant = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("Same Instant Zone: " + sameInstant);
    }

    @Test
    void getZonedDateTime() {
        // 获取当前时间的 ZonedDateTime 对象
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        // 获取当前日期在当年的第几天
        int dayOfYear = zonedDateTime.getDayOfYear();
        System.out.println("Day of Year: " + dayOfYear);

        // 获取当前日期在当月的第几天
        int dayOfMonth = zonedDateTime.getDayOfMonth();
        System.out.println("Day of Month: " + dayOfMonth);

        // 获取当前日期是星期几的枚举值
        DayOfWeek dayOfWeek = zonedDateTime.getDayOfWeek();
        System.out.println("Day of Week: " + dayOfWeek);

        // 获取当前日期的月份枚举值
        Month month = zonedDateTime.getMonth();
        System.out.println("Month: " + month);

        // 获取当前日期的月份值（1-12）
        int monthValue = zonedDateTime.getMonthValue();
        System.out.println("Month Value: " + monthValue);

        // 获取当前日期的年份
        int year = zonedDateTime.getYear();
        System.out.println("Year: " + year);

        // 获取当前时间的小时数（0-23）
        int hour = zonedDateTime.getHour();
        System.out.println("Hour: " + hour);

        // 获取当前时间的分钟数（0-59）
        int minute = zonedDateTime.getMinute();
        System.out.println("Minute: " + minute);

        // 获取当前时间的秒数（0-59）
        int second = zonedDateTime.getSecond();
        System.out.println("Second: " + second);

        // 获取当前时间的纳秒数（0-999,999,999）
        int nano = zonedDateTime.getNano();
        System.out.println("Nano: " + nano);

        // 获取当前时区的偏移量
        ZoneOffset offset = zonedDateTime.getOffset();
        System.out.println("Offset: " + offset);

        // 转换为 LocalDate 对象，只包含日期部分
        LocalDate localDate = zonedDateTime.toLocalDate();
        System.out.println("Local Date: " + localDate);

        // 转换为 LocalTime 对象，只包含时间部分
        LocalTime localTime = zonedDateTime.toLocalTime();
        System.out.println("Local Time: " + localTime);

        // 转换为 LocalDateTime 对象，包含日期和时间部分，没有时区信息
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        System.out.println("Local DateTime: " + localDateTime);

        // 转换为 Instant 对象，表示从1970-01-01T00:00:00Z起的瞬时点
        Instant instant = zonedDateTime.toInstant();
        System.out.println("Instant: " + instant);
    }

    @Test
    void testIs() {
        ZonedDateTime now = ZonedDateTime.now();

        // 未来的日期时间
        ZonedDateTime futureDateTime = now.plusDays(1);
        boolean before = now.isBefore(futureDateTime);
        System.out.println("Is now before futureDateTime? " + before); // 输出 true

        // 输出 true，表示支持一天中的秒数
        boolean supportsSeconds = now.isSupported(ChronoField.SECOND_OF_DAY);
        System.out.println("Supports seconds of day? " + supportsSeconds);

        // 输出 false，表示不支持一年中的周数
        boolean supportsWeeks = now.isSupported(ChronoField.ALIGNED_WEEK_OF_YEAR);
        System.out.println("Supports weeks of year? " + supportsWeeks);

        // 过去的日期时间
        ZonedDateTime pastDateTime = now.minusHours(1);
        boolean after = now.isAfter(pastDateTime);
        System.out.println("Is now after pastDateTime? " + after); // 输出 true

        ZonedDateTime sameDateTime = ZonedDateTime.of(2024, 7, 30, 12, 0, 0, 0, ZoneId.systemDefault());
        boolean equal = now.isEqual(sameDateTime);
        System.out.println("Is now equal to sameDateTime? " + equal); // 输出 true，如果两个日期时间相等
    }
}
