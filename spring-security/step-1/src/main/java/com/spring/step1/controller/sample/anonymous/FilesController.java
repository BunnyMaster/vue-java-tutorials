package com.spring.step1.controller.sample.anonymous;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.FilesDto;
import com.spring.step1.bean.entity.FilesEntity;
import com.spring.step1.bean.vo.FilesVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 文件记录 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "文件记录", description = "文件记录相关接口")
@RestController
@RequestMapping("/api/anonymous/files")
@RequiredArgsConstructor
public class FilesController {

    private final FilesService filesService;

    @Operation(summary = "分页查询文件记录", description = "分页文件记录")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<FilesVo>> getFilesPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            FilesDto dto) {
        Page<FilesEntity> pageParams = new Page<>(page, limit);
        PageResult<FilesVo> pageResult = filesService.getFilesPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加文件记录", description = "添加文件记录")
    @PostMapping()
    public Result<String> addFiles(@Valid @RequestBody FilesDto dto) {
        filesService.addFiles(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新文件记录", description = "更新文件记录")
    @PutMapping()
    public Result<String> updateFiles(@Valid @RequestBody FilesDto dto) {
        filesService.updateFiles(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除文件记录", description = "删除文件记录")
    @DeleteMapping()
    public Result<String> deleteFiles(@RequestBody List<Long> ids) {
        filesService.deleteFiles(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}