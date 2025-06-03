package cn.bunny.service.job.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class JobHello2 implements Job {
    public void start() {
        log.error("执行任务---JobHello2。。。。。。。。。");
        System.out.print("执行任务--JobHello2。。。。。。。。。");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        start();
    }
}
