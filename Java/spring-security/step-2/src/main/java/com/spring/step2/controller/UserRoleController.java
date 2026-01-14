package com.spring.step2.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.user.AssignUserRoleDto;
import com.spring.step2.domain.dto.user.UserRoleDto;
import com.spring.step2.domain.entity.UserRoleEntity;
import com.spring.step2.domain.vo.UserRoleVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Tag(name = "用户角色关联表", description = "用户角色关联表相关接口")
@RestController
@RequestMapping("/api/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Operation(summary = "分页查询用户角色关联表", description = "分页用户角色关联表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<UserRoleVo>> getUserRolePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            UserRoleDto dto) {
        Page<UserRoleEntity> pageParams = new Page<>(page, limit);
        PageResult<UserRoleVo> pageResult = userRoleService.getUserRolePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据用户id获取当前用户角色列表", description = "根据用户id获取当前用户角色列表")
    @GetMapping("roles")
    public Result<List<UserRoleVo>> getRoleListByUserId(Long userId) {
        List<UserRoleVo> voList = userRoleService.getRoleListByUserId(userId);
        return Result.success(voList);
    }

    @Operation(summary = "添加用户角色关联表", description = "添加用户角色关联表")
    @PostMapping()
    public Result<String> addUserRole(@Valid @RequestBody UserRoleDto dto) {
        userRoleService.addUserRole(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "为用户分配角色id", description = "根据用户id分配用户角色")
    @PostMapping("assign-role")
    public Result<String> assignUserRole(@Valid @RequestBody AssignUserRoleDto dto) {
        userRoleService.assignUserRole(dto);
        return Result.success();
    }

    @Operation(summary = "更新用户角色关联表", description = "更新用户角色关联表")
    @PutMapping()
    public Result<String> updateUserRole(@Valid @RequestBody UserRoleDto dto) {
        userRoleService.updateUserRole(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除用户角色关联表", description = "删除用户角色关联表")
    @DeleteMapping()
    public Result<String> deleteUserRole(@RequestBody List<Long> ids) {
        userRoleService.deleteUserRole(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}