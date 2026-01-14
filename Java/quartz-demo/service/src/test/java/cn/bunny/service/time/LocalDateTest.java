package cn.bunny.service.time;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.IsoChronology;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

public class LocalDateTest {
    @Test
    void testLocalDate() {
        LocalDate now = LocalDate.now();
        // 2024-07-29
        System.out.println(now);

        LocalDate plusLocateDate = now.plusMonths(1);
        // 2024-08-29
        System.out.println(plusLocateDate);

        // 2099-02-12
        LocalDate localDate = LocalDate.of(2099, 2, 12);
        System.out.println(localDate);
    }

    @Test
    void testLocalDateWith() {
        LocalDate parse = LocalDate.of(2001, 2, 12);

        // 将parse日期的月份修改为1月
        LocalDate withMonth = parse.withMonth(1);
        // 2001-01-12
        System.out.println(withMonth);

        // 将parse日期的月份修改为 2024 年
        LocalDate localDate = parse.withYear(2024);
        // 2024-02-12
        System.out.println(localDate);

        // 将parse日期的月份修改为 年份的第一天
        LocalDate withDayOfYear = parse.withDayOfYear(1);
        // 2001-01-01
        System.out.println(withDayOfYear);

        // TemporalField和对应的值来指定要修改的字段和新的值
        LocalDate with = parse.withYear(2024);
        // LocalDate with = parse.with(ChronoField.YEAR, 2024);
        System.out.println(with);
    }

    @Test
    void testLocalDateGet() {
        LocalDate parse = LocalDate.of(2001, 2, 12);

        // 获取日期的年表（Chronology），通常是ISO
        IsoChronology chronology = parse.getChronology();
        System.out.println(chronology);

        // 获取日期的纪元（Era），通常是CE（Common Era）
        IsoEra era = parse.getEra();
        System.out.println(era);

        // 获取日期的年份
        long parseLong = parse.getLong(ChronoField.YEAR);
        System.out.println(parseLong);

        // 获取日期的年份
        int year = parse.getYear();
        System.out.println(year);

        // 获取日期是星期几
        DayOfWeek dayOfWeek = parse.getDayOfWeek();
        int value = dayOfWeek.getValue();
        System.out.println(value);

        // 获取日期是当月的第几天
        int dayOfMonth = parse.getDayOfMonth();
        System.out.println(dayOfMonth);

        // 获取日期是当年的第几天
        int dayOfYear = parse.getDayOfYear();
        System.out.println(dayOfYear);

        // 获取日期的月份
        Month month = parse.getMonth();
        int monthValue = month.getValue();
        System.out.println(monthValue);

        // 获取日期的月份
        int parseMonthValue = parse.getMonthValue();
        System.out.println(parseMonthValue);

        // 获取指定字段的有效值范围
        // 对象将包含年份字段的有效值范围，可以用于确定该字段的最小和最大值。
        ValueRange valueRange = parse.range(ChronoField.YEAR);
        System.out.println(valueRange.getMinimum());
        System.out.println(valueRange.getMaximum());
    }

    @Test
    void testLocalDateIs() {
        LocalDate parse = LocalDate.of(2001, 2, 12);

        // 判断日期是否在指定日期之后
        boolean isAfter = parse.isAfter(LocalDate.of(2000, 1, 1));
        System.out.println("Is after 2000-01-01: " + isAfter);

        // 判断日期是否在指定日期之前
        boolean isBefore = parse.isBefore(LocalDate.of(2020, 1, 1));
        System.out.println("Is before 2020-01-01: " + isBefore);

        // 判断日期是否与指定日期相等
        boolean isEqual = parse.isEqual(LocalDate.of(2001, 2, 12));
        System.out.println("Is equal to 2001-02-12: " + isEqual);

        // 判断是否支持指定的字段
        boolean isSupported = parse.isSupported(ChronoField.DAY_OF_MONTH);
        System.out.println("Is supported DAY_OF_MONTH: " + isSupported);

        // 判断是否为闰年
        boolean isLeapYear = parse.isLeapYear();
        System.out.println("Is leap year: " + isLeapYear);
    }
}
