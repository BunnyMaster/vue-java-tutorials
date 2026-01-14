package com.spring.step1.controller.sample.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.UserDto;
import com.spring.step1.bean.entity.UserEntity;
import com.spring.step1.bean.vo.UserVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "用户信息", description = "用户信息相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户信息", description = "分页用户信息")
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

    @Operation(summary = "添加用户信息", description = "添加用户信息")
    @PostMapping()
    public Result<String> addUser(@Valid @RequestBody UserDto dto) {
        userService.addUser(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @PutMapping()
    public Result<String> updateUser(@Valid @RequestBody UserDto dto) {
        userService.updateUser(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除用户信息", description = "删除用户信息")
    @DeleteMapping()
    public Result<String> deleteUser(@RequestBody List<Long> ids) {
        userService.deleteUser(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}