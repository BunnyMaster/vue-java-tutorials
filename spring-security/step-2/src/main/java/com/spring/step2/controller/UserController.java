package com.spring.step2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.UserDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@Tag(name = "用户", description = "用户相关接口")
@RestController
@RequestMapping("/api/user/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户", description = "分页用户")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<UserVo>> getUserPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            UserDto dto) {
        Page<UserEntity> pageParams = new Page<>(page, limit);
        PageResult<UserVo> pageResult = userService.getUserPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加用户", description = "添加用户")
    @PostMapping()
    public Result<String> addUser(@Valid @RequestBody UserDto dto) {
        userService.addUser(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新用户", description = "更新用户")
    @PutMapping()
    public Result<String> updateUser(@Valid @RequestBody UserDto dto) {
        userService.updateUser(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @DeleteMapping()
    public Result<String> deleteUser(@RequestBody List<Long> ids) {
        userService.deleteUser(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}