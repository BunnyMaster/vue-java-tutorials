package cn.bunny.service.job;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class MyJobTest {
    @Test
    void testJob() throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 将自己写的类放入
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).build();

        // 设置执行时间
        SimpleTrigger build = TriggerBuilder.newTrigger().startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
                .build();

        // 开始执行
        scheduler.scheduleJob(jobDetail, build);
        scheduler.start();

        TimeUnit.MINUTES.sleep(10000);
        scheduler.shutdown();
    }

    @Test
    void test3() throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 将自己写的类放入
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("something", "一些内容");
        JobDetail jobDetail = JobBuilder.newJob(MyJobMap.class)
                .usingJobData("username", "呵呵呵")
                .usingJobData("password", "1223333")
                .usingJobData("age", 1)
                .usingJobData(jobDataMap)
                .build();

        // 设置执行时间
        SimpleTrigger build = TriggerBuilder.newTrigger().startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
                .build();

        // 开始执行
        scheduler.scheduleJob(jobDetail, build);
        scheduler.start();

        // 终止执行
        TimeUnit.MINUTES.sleep(1000);
        scheduler.shutdown();
    }

    @Test
    void test4() throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 将自己写的类放入
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("something", "一些内容");
        JobDetail jobDetail = JobBuilder.newJob(MyJobMap.class)
                .usingJobData(jobDataMap)
                .usingJobData("username", "呵呵呵")
                .usingJobData("password", "1223333")
                .usingJobData("age", 1)
                .build();

        // 设置开始时间和结束时间
        Calendar calendar = Calendar.getInstance();
        Date startTime = calendar.getTime();
        calendar.add(Calendar.SECOND, 5);
        Date endTIme = calendar.getTime();

        // 设置执行时间
        SimpleTrigger build = TriggerBuilder
                .newTrigger()
                // .startNow()
                .startAt(startTime)
                .endAt(endTIme)
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                // 每隔1秒执行一次
                                .withIntervalInSeconds(1)
                                // 执行3次
                                .withRepeatCount(3)
                        // .repeatForever()
                )
                .build();

        // 开始执行
        scheduler.scheduleJob(jobDetail, build);
        scheduler.start();

        TimeUnit.MINUTES.sleep(10000);
        scheduler.shutdown();
    }
}