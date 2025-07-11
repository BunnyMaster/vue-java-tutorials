package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.EmailTemplateDto;
import com.spring.official.domain.entity.EmailTemplateEntity;
import com.spring.official.domain.vo.EmailTemplateVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 邮件模板表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "邮件模板表", description = "邮件模板表相关接口")
@RestController
@RequestMapping("/api/system/email-template")
@RequiredArgsConstructor
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    @Operation(summary = "分页查询邮件模板表", description = "分页邮件模板表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<EmailTemplateVo>> getEmailTemplatePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            EmailTemplateDto dto) {
        Page<EmailTemplateEntity> pageParams = new Page<>(page, limit);
        PageResult<EmailTemplateVo> pageResult = emailTemplateService.getEmailTemplatePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加邮件模板表", description = "添加邮件模板表")
    @PostMapping()
    public Result<String> addEmailTemplate(@Valid @RequestBody EmailTemplateDto dto) {
        emailTemplateService.addEmailTemplate(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新邮件模板表", description = "更新邮件模板表")
    @PutMapping()
    public Result<String> updateEmailTemplate(@Valid @RequestBody EmailTemplateDto dto) {
        emailTemplateService.updateEmailTemplate(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除邮件模板表", description = "删除邮件模板表")
    @DeleteMapping()
    public Result<String> deleteEmailTemplate(@RequestBody List<Long> ids) {
        emailTemplateService.deleteEmailTemplate(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}