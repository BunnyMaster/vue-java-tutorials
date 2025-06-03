package cn.bunny.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
// @DisallowConcurrentExecution// 禁止并发执行
@PersistJobDataAfterExecution// 持久化，保持状态
public class MyJobCron implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
                .getJobDataMap();

        Integer someKey = (Integer) jobDataMap.get("age");

        log.error("执行MyJobMap---->{}", jobDataMap.get("username"));
        log.error("触发器内容--->{}", jobExecutionContext.getTrigger().getJobDataMap().get("someKey"));
        jobDataMap.put("age", ++someKey);

        // 修改触发器变量没有变化
        jobExecutionContext.getTrigger().getJobDataMap().put("someKey", ++someKey);
    }
}
