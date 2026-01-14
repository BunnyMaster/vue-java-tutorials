package cn.bunny.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;

@Slf4j
public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.error("Executing job " + new SimpleDateFormat("yyyy-MM-dd").format(context.getFireTime()));
    }
}
