package com.spring.step1.controller.sample.anonymous;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.RouterDto;
import com.spring.step1.bean.entity.RouterEntity;
import com.spring.step1.bean.vo.RouterVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.RouterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统菜单表", description = "系统菜单表相关接口")
@RestController
@RequestMapping("/api/anonymous/router")
@RequiredArgsConstructor
public class RouterController {

    private final RouterService routerService;

    @Operation(summary = "分页查询系统菜单表", description = "分页系统菜单表")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<RouterVo>> getRouterPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            RouterDto dto) {
        Page<RouterEntity> pageParams = new Page<>(page, limit);
        PageResult<RouterVo> pageResult = routerService.getRouterPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加系统菜单表", description = "添加系统菜单表")
    @PostMapping()
    public Result<String> addRouter(@Valid @RequestBody RouterDto dto) {
        routerService.addRouter(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统菜单表", description = "更新系统菜单表")
    @PutMapping()
    public Result<String> updateRouter(@Valid @RequestBody RouterDto dto) {
        routerService.updateRouter(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统菜单表", description = "删除系统菜单表")
    @DeleteMapping()
    public Result<String> deleteRouter(@RequestBody List<Long> ids) {
        routerService.deleteRouter(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}