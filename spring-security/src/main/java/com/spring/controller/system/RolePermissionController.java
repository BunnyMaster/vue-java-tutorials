package com.spring.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.domain.dto.system.RolePermissionDto;
import com.spring.domain.entity.RolePermissionEntity;
import com.spring.domain.vo.RolePermissionVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import com.spring.service.system.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统角色权限表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Tag(name = "系统角色权限表", description = "系统角色权限表相关接口")
@RestController
@RequestMapping("/api/product/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Operation(summary = "分页查询系统角色权限表", description = "分页系统角色权限表")
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

    @Operation(summary = "添加系统角色权限表", description = "添加系统角色权限表")
    @PostMapping()
    public Result<String> addRolePermission(@Valid @RequestBody RolePermissionDto dto) {
        rolePermissionService.addRolePermission(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统角色权限表", description = "更新系统角色权限表")
    @PutMapping()
    public Result<String> updateRolePermission(@Valid @RequestBody RolePermissionDto dto) {
        rolePermissionService.updateRolePermission(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统角色权限表", description = "删除系统角色权限表")
    @DeleteMapping()
    public Result<String> deleteRolePermission(@RequestBody List<Long> ids) {
        rolePermissionService.deleteRolePermission(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}