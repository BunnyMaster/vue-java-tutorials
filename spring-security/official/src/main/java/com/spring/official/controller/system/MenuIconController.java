package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.MenuIconDto;
import com.spring.official.domain.entity.MenuIconEntity;
import com.spring.official.domain.vo.MenuIconVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.MenuIconService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 图标code不能重复 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "图标code不能重复", description = "图标code不能重复相关接口")
@RestController
@RequestMapping("/api/system/menu-icon")
@RequiredArgsConstructor
public class MenuIconController {

    private final MenuIconService menuIconService;

    @Operation(summary = "分页查询图标code不能重复", description = "分页图标code不能重复")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<MenuIconVo>> getMenuIconPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            MenuIconDto dto) {
        Page<MenuIconEntity> pageParams = new Page<>(page, limit);
        PageResult<MenuIconVo> pageResult = menuIconService.getMenuIconPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加图标code不能重复", description = "添加图标code不能重复")
    @PostMapping()
    public Result<String> addMenuIcon(@Valid @RequestBody MenuIconDto dto) {
        menuIconService.addMenuIcon(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新图标code不能重复", description = "更新图标code不能重复")
    @PutMapping()
    public Result<String> updateMenuIcon(@Valid @RequestBody MenuIconDto dto) {
        menuIconService.updateMenuIcon(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除图标code不能重复", description = "删除图标code不能重复")
    @DeleteMapping()
    public Result<String> deleteMenuIcon(@RequestBody List<Long> ids) {
        menuIconService.deleteMenuIcon(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}