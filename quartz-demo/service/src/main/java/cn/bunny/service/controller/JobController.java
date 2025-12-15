package cn.bunny.service.controller;

import cn.bunny.dto.QuartzAddDto;
import cn.bunny.dto.QuartzOperationDto;
import cn.bunny.entity.quartz.JobList;
import cn.bunny.pojo.result.Result;
import cn.bunny.service.service.JobService;
import cn.bunny.vo.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quartz定时任务")
@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Operation(summary = "分页查询所有任务", description = "分页查询所有任务")
    @PostMapping("/page/{page}/{limit}")
    public Result<PageResult<JobList>> pageQuery(
            @PathVariable Integer page,
            @PathVariable Integer limit
    ) {
        Page<JobList> pageParams = new Page<>(page, limit);
        PageResult<JobList> pageResult = jobService.pageQuery(pageParams);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加任务", description = "添加任务")
    @PostMapping("/add")
    public Result<String> addJob(@RequestBody QuartzAddDto dto) {
        jobService.addJob(dto);
        return Result.success();
    }

    @Operation(summary = "暂停任务", description = "暂停任务")
    @PutMapping("/pause")
    public Result<String> pause(@RequestBody QuartzOperationDto dto) {
        jobService.pause(dto);
        return Result.success();
    }

    @Operation(summary = "恢复任务", description = "恢复任务")
    @PutMapping("/resume")
    public Result<String> resume(@RequestBody QuartzOperationDto dto) {
        jobService.resume(dto);
        return Result.success();
    }

    @Operation(summary = "移出任务", description = "移出任务")
    @DeleteMapping("/remove")
    public Result<String> remove(@RequestBody QuartzOperationDto dto) {
        jobService.remove(dto);
        return Result.success();
    }
}