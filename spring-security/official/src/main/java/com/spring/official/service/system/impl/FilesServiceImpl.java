package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.FilesDto;
import com.spring.official.domain.entity.FilesEntity;
import com.spring.official.domain.vo.FilesVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.FilesMapper;
import com.spring.official.service.system.FilesService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 文件记录 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class FilesServiceImpl extends ServiceImpl<FilesMapper, FilesEntity> implements FilesService {

    /**
     * 文件记录 服务实现类
     *
     * @param pageParams 文件记录分页查询page对象
     * @param dto        文件记录分页查询对象
     * @return 查询分页文件记录返回对象
     */
    @Override
    public PageResult<FilesVo> getFilesPage(Page<FilesEntity> pageParams, FilesDto dto) {
        IPage<FilesVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<FilesVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加文件记录
     *
     * @param dto 文件记录添加
     */
    @Override
    public void addFiles(FilesDto dto) {
        FilesEntity files = new FilesEntity();
        BeanUtils.copyProperties(dto, files);
        save(files);
    }

    /**
     * 更新文件记录
     *
     * @param dto 文件记录更新
     */
    @Override
    public void updateFiles(FilesDto dto) {
        FilesEntity files = new FilesEntity();
        BeanUtils.copyProperties(dto, files);
        updateById(files);
    }

    /**
     * 删除|批量删除文件记录
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteFiles(List<Long> ids) {
        removeByIds(ids);
    }
}