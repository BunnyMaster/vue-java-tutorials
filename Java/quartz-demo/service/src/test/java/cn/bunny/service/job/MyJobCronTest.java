package cn.bunny.service.job;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

class MyJobCronTest {
    @Test
    void testCron() throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 在计时中放入值
        HashMap<String, Object> map = new HashMap<>();
        map.put("something", "一些内容");
        map.put("username", "呵呵呵");
        map.put("password", "1223333");
        map.put("age", 1);
        JobDataMap jobDataMap = new JobDataMap(map);

        // 创建任务详情
        JobDetail jobDetail = JobBuilder.newJob(MyJobCron.class)
                .usingJobData(jobDataMap)
                .build();

        // 创建触发器
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();

        // 为触发器添加字段
        trigger.getJobDataMap().put("someKey", 1);

        // 开启定时器
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

        // 守护线程
        TimeUnit.MINUTES.sleep(10000);
        scheduler.shutdown();
    }
}