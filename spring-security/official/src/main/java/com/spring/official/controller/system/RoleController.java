package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.RoleDto;
import com.spring.official.domain.entity.RoleEntity;
import com.spring.official.domain.vo.RoleVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统角色表", description = "系统角色表相关接口")
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "分页查询系统角色表", description = "分页系统角色表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<RoleVo>> getRolePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            RoleDto dto) {
        Page<RoleEntity> pageParams = new Page<>(page, limit);
        PageResult<RoleVo> pageResult = roleService.getRolePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加系统角色表", description = "添加系统角色表")
    @PostMapping()
    public Result<String> addRole(@Valid @RequestBody RoleDto dto) {
        roleService.addRole(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统角色表", description = "更新系统角色表")
    @PutMapping()
    public Result<String> updateRole(@Valid @RequestBody RoleDto dto) {
        roleService.updateRole(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统角色表", description = "删除系统角色表")
    @DeleteMapping()
    public Result<String> deleteRole(@RequestBody List<Long> ids) {
        roleService.deleteRole(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}