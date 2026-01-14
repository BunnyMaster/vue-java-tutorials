package com.spring.step1.controller.sample.anonymous;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.I18nTypeDto;
import com.spring.step1.bean.entity.I18nTypeEntity;
import com.spring.step1.bean.vo.I18nTypeVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.I18nTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 多语言类型表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "多语言类型表", description = "多语言类型表相关接口")
@RestController
@RequestMapping("/api/anonymous/i18n-type")
@RequiredArgsConstructor
public class I18nTypeController {

    private final I18nTypeService i18nTypeService;

    @Operation(summary = "分页查询多语言类型表", description = "分页多语言类型表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<I18nTypeVo>> getI18nTypePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            I18nTypeDto dto) {
        Page<I18nTypeEntity> pageParams = new Page<>(page, limit);
        PageResult<I18nTypeVo> pageResult = i18nTypeService.getI18nTypePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加多语言类型表", description = "添加多语言类型表")
    @PostMapping()
    public Result<String> addI18nType(@Valid @RequestBody I18nTypeDto dto) {
        i18nTypeService.addI18nType(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新多语言类型表", description = "更新多语言类型表")
    @PutMapping()
    public Result<String> updateI18nType(@Valid @RequestBody I18nTypeDto dto) {
        i18nTypeService.updateI18nType(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除多语言类型表", description = "删除多语言类型表")
    @DeleteMapping()
    public Result<String> deleteI18nType(@RequestBody List<Long> ids) {
        i18nTypeService.deleteI18nType(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}