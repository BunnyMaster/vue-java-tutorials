package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.FilesPartDetailDto;
import com.spring.domain.entity.FilesPartDetailEntity;
import com.spring.domain.vo.FilesPartDetailVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 文件分片信息表，仅在手动分片上传时使用 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface FilesPartDetailService extends IService<FilesPartDetailEntity> {

    /**
     * 分页查询文件分片信息表，仅在手动分片上传时使用
     *
     * @return {@link FilesPartDetailVo}
     */
    PageResult<FilesPartDetailVo> getFilesPartDetailPage(Page<FilesPartDetailEntity> pageParams, FilesPartDetailDto dto);

    /**
     * 添加文件分片信息表，仅在手动分片上传时使用
     *
     * @param dto {@link FilesPartDetailDto} 添加表单
     */
    void addFilesPartDetail(FilesPartDetailDto dto);

    /**
     * 更新文件分片信息表，仅在手动分片上传时使用
     *
     * @param dto {@link FilesPartDetailDto} 更新表单
     */
    void updateFilesPartDetail(FilesPartDetailDto dto);

    /**
     * 删除|批量删除文件分片信息表，仅在手动分片上传时使用类型
     *
     * @param ids 删除id列表
     */
    void deleteFilesPartDetail(List<Long> ids);
}
