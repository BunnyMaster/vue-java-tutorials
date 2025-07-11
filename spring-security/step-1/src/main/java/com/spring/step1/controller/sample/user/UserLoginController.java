package com.spring.step1.controller.sample.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.UserLoginDto;
import com.spring.step1.bean.entity.UserLoginEntity;
import com.spring.step1.bean.vo.UserLoginVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户登录日志 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "用户登录日志", description = "用户登录日志相关接口")
@RestController
@RequestMapping("/api/user/user-login")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;

    @Operation(summary = "分页查询用户登录日志", description = "分页用户登录日志")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<UserLoginVo>> getUserLoginPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            UserLoginDto dto) {
        Page<UserLoginEntity> pageParams = new Page<>(page, limit);
        PageResult<UserLoginVo> pageResult = userLoginService.getUserLoginPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加用户登录日志", description = "添加用户登录日志")
    @PostMapping()
    public Result<String> addUserLogin(@Valid @RequestBody UserLoginDto dto) {
        userLoginService.addUserLogin(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新用户登录日志", description = "更新用户登录日志")
    @PutMapping()
    public Result<String> updateUserLogin(@Valid @RequestBody UserLoginDto dto) {
        userLoginService.updateUserLogin(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除用户登录日志", description = "删除用户登录日志")
    @DeleteMapping()
    public Result<String> deleteUserLogin(@RequestBody List<Long> ids) {
        userLoginService.deleteUserLogin(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}