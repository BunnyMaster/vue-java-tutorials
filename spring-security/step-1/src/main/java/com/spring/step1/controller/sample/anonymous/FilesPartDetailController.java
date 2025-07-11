package com.spring.step1.controller.sample.anonymous;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.FilesPartDetailDto;
import com.spring.step1.bean.entity.FilesPartDetailEntity;
import com.spring.step1.bean.vo.FilesPartDetailVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.FilesPartDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 文件分片信息表，仅在手动分片上传时使用 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "文件分片信息表，仅在手动分片上传时使用", description = "文件分片信息表，仅在手动分片上传时使用相关接口")
@RestController
@RequestMapping("/api/anonymous/files-part-detail")
@RequiredArgsConstructor
public class FilesPartDetailController {

    private final FilesPartDetailService filesPartDetailService;

    @Operation(summary = "分页查询文件分片信息表，仅在手动分片上传时使用", description = "分页文件分片信息表，仅在手动分片上传时使用")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<FilesPartDetailVo>> getFilesPartDetailPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            FilesPartDetailDto dto) {
        Page<FilesPartDetailEntity> pageParams = new Page<>(page, limit);
        PageResult<FilesPartDetailVo> pageResult = filesPartDetailService.getFilesPartDetailPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加文件分片信息表，仅在手动分片上传时使用", description = "添加文件分片信息表，仅在手动分片上传时使用")
    @PostMapping()
    public Result<String> addFilesPartDetail(@Valid @RequestBody FilesPartDetailDto dto) {
        filesPartDetailService.addFilesPartDetail(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新文件分片信息表，仅在手动分片上传时使用", description = "更新文件分片信息表，仅在手动分片上传时使用")
    @PutMapping()
    public Result<String> updateFilesPartDetail(@Valid @RequestBody FilesPartDetailDto dto) {
        filesPartDetailService.updateFilesPartDetail(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除文件分片信息表，仅在手动分片上传时使用", description = "删除文件分片信息表，仅在手动分片上传时使用")
    @DeleteMapping()
    public Result<String> deleteFilesPartDetail(@RequestBody List<Long> ids) {
        filesPartDetailService.deleteFilesPartDetail(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}