package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.I18nTypeDto;
import com.spring.official.domain.entity.I18nTypeEntity;
import com.spring.official.domain.vo.I18nTypeVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.I18nTypeMapper;
import com.spring.official.service.system.I18nTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 多语言类型表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class I18nTypeServiceImpl extends ServiceImpl<I18nTypeMapper, I18nTypeEntity> implements I18nTypeService {

    /**
     * 多语言类型表 服务实现类
     *
     * @param pageParams 多语言类型表分页查询page对象
     * @param dto        多语言类型表分页查询对象
     * @return 查询分页多语言类型表返回对象
     */
    @Override
    public PageResult<I18nTypeVo> getI18nTypePage(Page<I18nTypeEntity> pageParams, I18nTypeDto dto) {
        IPage<I18nTypeVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<I18nTypeVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加多语言类型表
     *
     * @param dto 多语言类型表添加
     */
    @Override
    public void addI18nType(I18nTypeDto dto) {
        I18nTypeEntity i18nType = new I18nTypeEntity();
        BeanUtils.copyProperties(dto, i18nType);
        save(i18nType);
    }

    /**
     * 更新多语言类型表
     *
     * @param dto 多语言类型表更新
     */
    @Override
    public void updateI18nType(I18nTypeDto dto) {
        I18nTypeEntity i18nType = new I18nTypeEntity();
        BeanUtils.copyProperties(dto, i18nType);
        updateById(i18nType);
    }

    /**
     * 删除|批量删除多语言类型表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteI18nType(List<Long> ids) {
        removeByIds(ids);
    }
}