package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.I18nDto;
import com.spring.official.domain.entity.I18nEntity;
import com.spring.official.domain.vo.I18nVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.I18nService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 多语言表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "多语言表", description = "多语言表相关接口")
@RestController
@RequestMapping("/api/system/i18n")
@RequiredArgsConstructor
public class I18nController {

    private final I18nService i18nService;

    @Operation(summary = "分页查询多语言表", description = "分页多语言表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<I18nVo>> getI18nPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            I18nDto dto) {
        Page<I18nEntity> pageParams = new Page<>(page, limit);
        PageResult<I18nVo> pageResult = i18nService.getI18nPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加多语言表", description = "添加多语言表")
    @PostMapping()
    public Result<String> addI18n(@Valid @RequestBody I18nDto dto) {
        i18nService.addI18n(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新多语言表", description = "更新多语言表")
    @PutMapping()
    public Result<String> updateI18n(@Valid @RequestBody I18nDto dto) {
        i18nService.updateI18n(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除多语言表", description = "删除多语言表")
    @DeleteMapping()
    public Result<String> deleteI18n(@RequestBody List<Long> ids) {
        i18nService.deleteI18n(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}