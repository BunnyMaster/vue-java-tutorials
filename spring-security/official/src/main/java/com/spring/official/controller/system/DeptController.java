package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.DeptDto;
import com.spring.official.domain.entity.DeptEntity;
import com.spring.official.domain.vo.DeptVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "部门表", description = "部门表相关接口")
@RestController
@RequestMapping("/api/system/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "分页查询部门表", description = "分页部门表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<DeptVo>> getDeptPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            DeptDto dto) {
        Page<DeptEntity> pageParams = new Page<>(page, limit);
        PageResult<DeptVo> pageResult = deptService.getDeptPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加部门表", description = "添加部门表")
    @PostMapping()
    public Result<String> addDept(@Valid @RequestBody DeptDto dto) {
        deptService.addDept(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新部门表", description = "更新部门表")
    @PutMapping()
    public Result<String> updateDept(@Valid @RequestBody DeptDto dto) {
        deptService.updateDept(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除部门表", description = "删除部门表")
    @DeleteMapping()
    public Result<String> deleteDept(@RequestBody List<Long> ids) {
        deptService.deleteDept(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}