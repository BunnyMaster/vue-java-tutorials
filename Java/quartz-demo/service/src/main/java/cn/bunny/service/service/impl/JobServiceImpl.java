package cn.bunny.service.service.impl;

import cn.bunny.dto.QuartzAddDto;
import cn.bunny.dto.QuartzOperationDto;
import cn.bunny.entity.quartz.JobList;
import cn.bunny.service.mapper.JobListMapper;
import cn.bunny.service.service.JobService;
import cn.bunny.vo.PageResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;

@SuppressWarnings("unchecked")
@Service
@DS("quartz")
@Slf4j
public class JobServiceImpl extends ServiceImpl<JobListMapper, JobList> implements JobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * * 添加任务
     *
     * @param dto 添加任务
     */
    @SneakyThrows
    @Override
    public void addJob(QuartzAddDto dto) {
        String jobGroup = dto.getJobGroup();
        String jobName = dto.getJobName();
        String cronExpression = dto.getCronExpression();
        String description = dto.getDescription();
        String jobMethodName = dto.getJobMethodName();
        String jobClassName = dto.getJobClassName();


        // 动态创建Class对象
        Class<?> className = Class.forName(jobClassName);
        Constructor<?> constructor = className.getConstructor(); // 获取无参构造函数
        constructor.newInstance(); // 创建实例

        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) className).withIdentity(jobName, jobGroup)
                .withDescription(description).build();
        jobDetail.getJobDataMap().put("jobMethodName", jobMethodName);

        // 执行任务
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + jobName, jobGroup)
                .startNow().withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * * 暂停任务
     *
     * @param dto 暂停任务
     */
    @SneakyThrows
    @Override
    public void pause(QuartzOperationDto dto) {
        JobKey key = new JobKey(dto.getJobName(), dto.getJobGroup());
        scheduler.pauseJob(key);
    }

    /**
     * * 恢复任务
     *
     * @param dto 恢复任务
     */
    @SneakyThrows
    @Override
    public void resume(QuartzOperationDto dto) {
        JobKey key = new JobKey(dto.getJobName(), dto.getJobGroup());
        scheduler.resumeJob(key);
    }

    /**
     * * 移出任务
     *
     * @param dto 移出任务
     */
    @SneakyThrows
    @Override
    public void remove(QuartzOperationDto dto) {
        String jobGroup = dto.getJobGroup();
        String jobName = dto.getJobName();

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));

        log.warn("删除成功");
    }

    /**
     * * 分页查询所有任务
     *
     * @param pageParams 分页删除
     * @return 分页结果
     */
    @Override
    public PageResult<JobList> pageQuery(Page<JobList> pageParams) {
        Page<JobList> listPage = page(pageParams);

        return PageResult.<JobList>builder()
                .pageNo((int) listPage.getCurrent())
                .pageSize((int) listPage.getSize())
                .total(listPage.getTotal())
                .list(listPage.getRecords())
                .build();
    }
}
// SELECT
// 	`job`.`JOB_NAME` AS `JOB_NAME`,
//         `job`.`JOB_GROUP` AS `JOB_GROUP`,
//         `job`.`DESCRIPTION` AS `DESCRIPTION`,
//         `job`.`JOB_CLASS_NAME` AS `JOB_CLASS_NAME`,
//         `cron`.`CRON_EXPRESSION` AS `CRON_EXPRESSION`,
//         `tri`.`TRIGGER_NAME` AS `TRIGGER_NAME`,
//         `tri`.`TRIGGER_STATE` AS `TRIGGER_STATE`
// FROM
//         ((
//                  `QRTZ_JOB_DETAILS` `job`
//                  JOIN `QRTZ_TRIGGERS` `tri` ON (((
//                  `job`.`JOB_NAME` = `tri`.`JOB_NAME`
//         )
// AND ( `job`.`JOB_GROUP` = `tri`.`JOB_GROUP` ))))
// JOIN `QRTZ_CRON_TRIGGERS` `cron` ON (((
//                                              `cron`.`TRIGGER_NAME` = `tri`.`TRIGGER_NAME`
// )
// AND ( `cron`.`TRIGGER_GROUP` = `tri`.`JOB_GROUP` ))))
// WHERE
//         (
// 	`tri`.`TRIGGER_TYPE` = 'CRON')