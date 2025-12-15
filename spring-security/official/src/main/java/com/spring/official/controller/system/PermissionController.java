package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.PermissionDto;
import com.spring.official.domain.entity.PermissionEntity;
import com.spring.official.domain.vo.PermissionVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.PermissionService;
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
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统权限表", description = "系统权限表相关接口")
@RestController
@RequestMapping("/api/system/permission")
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