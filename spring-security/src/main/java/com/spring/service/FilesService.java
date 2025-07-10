package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.FilesDto;
import com.spring.domain.entity.FilesEntity;
import com.spring.domain.vo.FilesVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 文件记录 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface FilesService extends IService<FilesEntity> {

    /**
     * 分页查询文件记录
     *
     * @return {@link FilesVo}
     */
    PageResult<FilesVo> getFilesPage(Page<FilesEntity> pageParams, FilesDto dto);

    /**
     * 添加文件记录
     *
     * @param dto {@link FilesDto} 添加表单
     */
    void addFiles(FilesDto dto);

    /**
     * 更新文件记录
     *
     * @param dto {@link FilesDto} 更新表单
     */
    void updateFiles(FilesDto dto);

    /**
     * 删除|批量删除文件记录类型
     *
     * @param ids 删除id列表
     */
    void deleteFiles(List<Long> ids);
}
