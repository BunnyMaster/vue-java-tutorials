package com.spring.step3.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step3.domain.dto.permission.PermissionDto;
import com.spring.step3.domain.entity.PermissionEntity;
import com.spring.step3.domain.vo.PermissionVo;
import com.spring.step3.domain.vo.result.PageResult;
import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.service.roles.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统权限表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Tag(name = "系统权限表", description = "系统权限表相关接口")
@RestController
@RequestMapping(value = "/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "分页查询系统权限表", description = "分页系统权限表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<PermissionVo>> getPermissionPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            PermissionDto dto) {
        Page<PermissionEntity> pageParams = new Page<>(page, limit);
        PageResult<PermissionVo> pageResult = permissionService.getPermissionPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "所有的权限列表", description = "获取所有的权限列表")
    @GetMapping("all")
    public Result<List<PermissionVo>> getAllPermission() {
        List<PermissionVo> voList = permissionService.getAllPermission();
        return Result.success(voList);
    }

    @Operation(summary = "添加系统权限表", description = "添加系统权限表")
    @PostMapping()
    public Result<String> addPermission(@Valid @RequestBody PermissionDto dto) {
        permissionService.addPermission(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统权限表", description = "更新系统权限表")
    @PutMapping()
    public Result<String> updatePermission(@Valid @RequestBody PermissionDto dto) {
        permissionService.updatePermission(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统权限表", description = "删除系统权限表")
    @DeleteMapping()
    public Result<String> deletePermission(@RequestBody List<Long> ids) {
        permissionService.deletePermission(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}