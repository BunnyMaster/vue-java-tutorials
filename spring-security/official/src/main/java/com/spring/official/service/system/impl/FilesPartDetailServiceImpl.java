package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.FilesPartDetailDto;
import com.spring.official.domain.entity.FilesPartDetailEntity;
import com.spring.official.domain.vo.FilesPartDetailVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.FilesPartDetailMapper;
import com.spring.official.service.system.FilesPartDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 文件分片信息表，仅在手动分片上传时使用 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class FilesPartDetailServiceImpl extends ServiceImpl<FilesPartDetailMapper, FilesPartDetailEntity> implements FilesPartDetailService {

    /**
     * 文件分片信息表，仅在手动分片上传时使用 服务实现类
     *
     * @param pageParams 文件分片信息表，仅在手动分片上传时使用分页查询page对象
     * @param dto        文件分片信息表，仅在手动分片上传时使用分页查询对象
     * @return 查询分页文件分片信息表，仅在手动分片上传时使用返回对象
     */
    @Override
    public PageResult<FilesPartDetailVo> getFilesPartDetailPage(Page<FilesPartDetailEntity> pageParams, FilesPartDetailDto dto) {
        IPage<FilesPartDetailVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<FilesPartDetailVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加文件分片信息表，仅在手动分片上传时使用
     *
     * @param dto 文件分片信息表，仅在手动分片上传时使用添加
     */
    @Override
    public void addFilesPartDetail(FilesPartDetailDto dto) {
        FilesPartDetailEntity filesPartDetail = new FilesPartDetailEntity();
        BeanUtils.copyProperties(dto, filesPartDetail);
        save(filesPartDetail);
    }

    /**
     * 更新文件分片信息表，仅在手动分片上传时使用
     *
     * @param dto 文件分片信息表，仅在手动分片上传时使用更新
     */
    @Override
    public void updateFilesPartDetail(FilesPartDetailDto dto) {
        FilesPartDetailEntity filesPartDetail = new FilesPartDetailEntity();
        BeanUtils.copyProperties(dto, filesPartDetail);
        updateById(filesPartDetail);
    }

    /**
     * 删除|批量删除文件分片信息表，仅在手动分片上传时使用
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteFilesPartDetail(List<Long> ids) {
        removeByIds(ids);
    }
}