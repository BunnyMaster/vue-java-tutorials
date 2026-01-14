package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.UserDeptDto;
import com.spring.official.domain.entity.UserDeptEntity;
import com.spring.official.domain.vo.UserDeptVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.UserDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门用户关系表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "部门用户关系表", description = "部门用户关系表相关接口")
@RestController
@RequestMapping("/api/system/user-dept")
@RequiredArgsConstructor
public class UserDeptController {

    private final UserDeptService userDeptService;

    @Operation(summary = "分页查询部门用户关系表", description = "分页部门用户关系表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<UserDeptVo>> getUserDeptPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            UserDeptDto dto) {
        Page<UserDeptEntity> pageParams = new Page<>(page, limit);
        PageResult<UserDeptVo> pageResult = userDeptService.getUserDeptPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加部门用户关系表", description = "添加部门用户关系表")
    @PostMapping()
    public Result<String> addUserDept(@Valid @RequestBody UserDeptDto dto) {
        userDeptService.addUserDept(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新部门用户关系表", description = "更新部门用户关系表")
    @PutMapping()
    public Result<String> updateUserDept(@Valid @RequestBody UserDeptDto dto) {
        userDeptService.updateUserDept(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除部门用户关系表", description = "删除部门用户关系表")
    @DeleteMapping()
    public Result<String> deleteUserDept(@RequestBody List<Long> ids) {
        userDeptService.deleteUserDept(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}