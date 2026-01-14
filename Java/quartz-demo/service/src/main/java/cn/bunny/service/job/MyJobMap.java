package cn.bunny.service.job;


import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
// 持久化存储内容，获取时比如对某个值进行附加操作
@PersistJobDataAfterExecution
public class MyJobMap implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
                .getJobDataMap();

        log.error("执行MyJobMap->{}", jobDataMap.get("username"));
        log.error("执行MyJobMap->{}", jobDataMap.get("password"));
        log.error("执行MyJobMap->{}", jobDataMap.get("something"));
        log.error("执行MyJobMap->{}", jobDataMap.get("age"));

        Integer age = (Integer) jobDataMap.get("age");
        System.out.println("啊啊啊啊啊啊啊啊_>" + age);
        jobExecutionContext.getJobDetail().getJobDataMap().put("age", ++age);

        Date startTime = jobExecutionContext.getTrigger().getStartTime();
        Date endTime = jobExecutionContext.getTrigger().getEndTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("开始时间->{}", simpleDateFormat.format(startTime));
        log.info("结束时间->{}", simpleDateFormat.format(endTime));
    }
}
