package com.spring.step2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.role.AssignRolePermissionDto;
import com.spring.step2.domain.dto.role.RolePermissionDto;
import com.spring.step2.domain.entity.RolePermissionEntity;
import com.spring.step2.domain.vo.RolePermissionVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Tag(name = "角色权限关联表", description = "角色权限关联表相关接口")
@RestController
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Operation(summary = "分页查询角色权限关联表", description = "分页角色权限关联表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<RolePermissionVo>> getRolePermissionPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            RolePermissionDto dto) {
        Page<RolePermissionEntity> pageParams = new Page<>(page, limit);
        PageResult<RolePermissionVo> pageResult = rolePermissionService.getRolePermissionPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @GetMapping("permissions")
    @Operation(summary = "根据角色id获取权限内容", description = "根据角色id获取权限内容")
    public Result<List<RolePermissionVo>> getRolePermissionById(Long permissionId) {
        List<RolePermissionVo> voList = rolePermissionService.getRolePermissionById(permissionId);
        return Result.success(voList);
    }

    @Operation(summary = "添加角色权限关联表", description = "添加角色权限关联表")
    @PostMapping()
    public Result<String> addRolePermission(@Valid @RequestBody RolePermissionDto dto) {
        rolePermissionService.addRolePermission(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "为角色分配权限", description = "根据角色id分配权限")
    @PostMapping("assign-permission")
    public Result<String> assignRolePermission(@Valid @RequestBody AssignRolePermissionDto dto) {
        rolePermissionService.assignRolePermission(dto);
        return Result.success();
    }

    @Operation(summary = "更新角色权限关联表", description = "更新角色权限关联表")
    @PutMapping()
    public Result<String> updateRolePermission(@Valid @RequestBody RolePermissionDto dto) {
        rolePermissionService.updateRolePermission(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除角色权限关联表", description = "删除角色权限关联表")
    @DeleteMapping()
    public Result<String> deleteRolePermission(@RequestBody List<Long> ids) {
        rolePermissionService.deleteRolePermission(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}