package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.I18nDto;
import com.spring.official.domain.entity.I18nEntity;
import com.spring.official.domain.vo.I18nVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.I18nMapper;
import com.spring.official.service.system.I18nService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 多语言表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class I18nServiceImpl extends ServiceImpl<I18nMapper, I18nEntity> implements I18nService {

    /**
     * 多语言表 服务实现类
     *
     * @param pageParams 多语言表分页查询page对象
     * @param dto        多语言表分页查询对象
     * @return 查询分页多语言表返回对象
     */
    @Override
    public PageResult<I18nVo> getI18nPage(Page<I18nEntity> pageParams, I18nDto dto) {
        IPage<I18nVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<I18nVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加多语言表
     *
     * @param dto 多语言表添加
     */
    @Override
    public void addI18n(I18nDto dto) {
        I18nEntity i18n = new I18nEntity();
        BeanUtils.copyProperties(dto, i18n);
        save(i18n);
    }

    /**
     * 更新多语言表
     *
     * @param dto 多语言表更新
     */
    @Override
    public void updateI18n(I18nDto dto) {
        I18nEntity i18n = new I18nEntity();
        BeanUtils.copyProperties(dto, i18n);
        updateById(i18n);
    }

    /**
     * 删除|批量删除多语言表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteI18n(List<Long> ids) {
        removeByIds(ids);
    }
}