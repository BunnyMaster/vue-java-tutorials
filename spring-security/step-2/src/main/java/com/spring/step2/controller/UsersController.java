package com.spring.step2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.UsersDto;
import com.spring.step2.domain.entity.UsersEntity;
import com.spring.step2.domain.vo.UsersVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@Tag(name = "Csharp用户接口查询", description = "用户接口查询相关接口")
@RestController
@RequestMapping("/api/user/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @Operation(summary = "分页查询", description = "分页")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<UsersVo>> getUsersPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            UsersDto dto) {
        Page<UsersEntity> pageParams = new Page<>(page, limit);
        PageResult<UsersVo> pageResult = usersService.getUsersPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加", description = "添加")
    @PostMapping()
    public Result<String> addUsers(@Valid @RequestBody UsersDto dto) {
        usersService.addUsers(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新", description = "更新")
    @PutMapping()
    public Result<String> updateUsers(@Valid @RequestBody UsersDto dto) {
        usersService.updateUsers(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除", description = "删除")
    @DeleteMapping()
    public Result<String> deleteUsers(@RequestBody List<Long> ids) {
        usersService.deleteUsers(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}