package cn.bunny.service.service;

import cn.bunny.dto.QuartzAddDto;
import cn.bunny.dto.QuartzOperationDto;
import cn.bunny.entity.quartz.JobList;
import cn.bunny.vo.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface JobService extends IService<JobList> {

    /**
     * * 添加任务
     *
     * @param dto 添加任务
     */
    void addJob(QuartzAddDto dto);

    /**
     * * 暂停任务
     *
     * @param dto 暂停任务
     */
    void pause(QuartzOperationDto dto);

    /**
     * * 恢复任务
     *
     * @param dto 恢复任务
     */
    void resume(QuartzOperationDto dto);

    /**
     * * 移出任务
     *
     * @param dto 移出任务
     */
    void remove(QuartzOperationDto dto);

    /**
     * * 分页查询所有任务
     *
     * @param pageParams 分页删除
     * @return 分页结果
     */
    PageResult<JobList> pageQuery(Page<JobList> pageParams);
}
