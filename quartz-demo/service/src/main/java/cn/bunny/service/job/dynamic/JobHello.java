package cn.bunny.service.job.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class JobHello implements Job {
    public void start() {
        log.error("执行任务--JobHello。。。。。。。。。");
        System.out.print("执行任务--JobHello。。。。。。。。。");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        start();
    }
}
