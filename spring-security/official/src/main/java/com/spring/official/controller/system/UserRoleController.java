package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.UserRoleDto;
import com.spring.official.domain.entity.UserRoleEntity;
import com.spring.official.domain.vo.UserRoleVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户角色关系表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统用户角色关系表", description = "系统用户角色关系表相关接口")
@RestController
@RequestMapping("/api/system/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Operation(summary = "分页查询系统用户角色关系表", description = "分页系统用户角色关系表")
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

    @Operation(summary = "添加系统用户角色关系表", description = "添加系统用户角色关系表")
    @PostMapping()
    public Result<String> addUserRole(@Valid @RequestBody UserRoleDto dto) {
        userRoleService.addUserRole(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统用户角色关系表", description = "更新系统用户角色关系表")
    @PutMapping()
    public Result<String> updateUserRole(@Valid @RequestBody UserRoleDto dto) {
        userRoleService.updateUserRole(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统用户角色关系表", description = "删除系统用户角色关系表")
    @DeleteMapping()
    public Result<String> deleteUserRole(@RequestBody List<Long> ids) {
        userRoleService.deleteUserRole(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}