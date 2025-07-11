package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.EmailUsersDto;
import com.spring.official.domain.entity.EmailUsersEntity;
import com.spring.official.domain.vo.EmailUsersVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.EmailUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 邮箱发送表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "邮箱发送表", description = "邮箱发送表相关接口")
@RestController
@RequestMapping("/api/system/email-users")
@RequiredArgsConstructor
public class EmailUsersController {

    private final EmailUsersService emailUsersService;

    @Operation(summary = "分页查询邮箱发送表", description = "分页邮箱发送表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<EmailUsersVo>> getEmailUsersPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            EmailUsersDto dto) {
        Page<EmailUsersEntity> pageParams = new Page<>(page, limit);
        PageResult<EmailUsersVo> pageResult = emailUsersService.getEmailUsersPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加邮箱发送表", description = "添加邮箱发送表")
    @PostMapping()
    public Result<String> addEmailUsers(@Valid @RequestBody EmailUsersDto dto) {
        emailUsersService.addEmailUsers(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新邮箱发送表", description = "更新邮箱发送表")
    @PutMapping()
    public Result<String> updateEmailUsers(@Valid @RequestBody EmailUsersDto dto) {
        emailUsersService.updateEmailUsers(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除邮箱发送表", description = "删除邮箱发送表")
    @DeleteMapping()
    public Result<String> deleteEmailUsers(@RequestBody List<Long> ids) {
        emailUsersService.deleteEmailUsers(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}