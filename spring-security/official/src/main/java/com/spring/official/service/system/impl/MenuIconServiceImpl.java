package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.MenuIconDto;
import com.spring.official.domain.entity.MenuIconEntity;
import com.spring.official.domain.vo.MenuIconVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.MenuIconMapper;
import com.spring.official.service.system.MenuIconService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 图标code不能重复 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:28
 */
@Service
@Transactional
public class MenuIconServiceImpl extends ServiceImpl<MenuIconMapper, MenuIconEntity> implements MenuIconService {

    /**
     * 图标code不能重复 服务实现类
     *
     * @param pageParams 图标code不能重复分页查询page对象
     * @param dto        图标code不能重复分页查询对象
     * @return 查询分页图标code不能重复返回对象
     */
    @Override
    public PageResult<MenuIconVo> getMenuIconPage(Page<MenuIconEntity> pageParams, MenuIconDto dto) {
        IPage<MenuIconVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<MenuIconVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加图标code不能重复
     *
     * @param dto 图标code不能重复添加
     */
    @Override
    public void addMenuIcon(MenuIconDto dto) {
        MenuIconEntity menuIcon = new MenuIconEntity();
        BeanUtils.copyProperties(dto, menuIcon);
        save(menuIcon);
    }

    /**
     * 更新图标code不能重复
     *
     * @param dto 图标code不能重复更新
     */
    @Override
    public void updateMenuIcon(MenuIconDto dto) {
        MenuIconEntity menuIcon = new MenuIconEntity();
        BeanUtils.copyProperties(dto, menuIcon);
        updateById(menuIcon);
    }

    /**
     * 删除|批量删除图标code不能重复
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteMenuIcon(List<Long> ids) {
        removeByIds(ids);
    }
}