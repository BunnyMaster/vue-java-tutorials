package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.RouterDto;
import com.spring.step1.bean.entity.RouterEntity;
import com.spring.step1.bean.vo.RouterVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.RouterMapper;
import com.spring.step1.service.sample.RouterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class RouterServiceImpl extends ServiceImpl<RouterMapper, RouterEntity> implements RouterService {

    /**
     * 系统菜单表 服务实现类
     *
     * @param pageParams 系统菜单表分页查询page对象
     * @param dto        系统菜单表分页查询对象
     * @return 查询分页系统菜单表返回对象
     */
    @Override
    public PageResult<RouterVo> getRouterPage(Page<RouterEntity> pageParams, RouterDto dto) {
        IPage<RouterVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<RouterVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统菜单表
     *
     * @param dto 系统菜单表添加
     */
    @Override
    public void addRouter(RouterDto dto) {
        RouterEntity router = new RouterEntity();
        BeanUtils.copyProperties(dto, router);
        save(router);
    }

    /**
     * 更新系统菜单表
     *
     * @param dto 系统菜单表更新
     */
    @Override
    public void updateRouter(RouterDto dto) {
        RouterEntity router = new RouterEntity();
        BeanUtils.copyProperties(dto, router);
        updateById(router);
    }

    /**
     * 删除|批量删除系统菜单表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteRouter(List<Long> ids) {
        removeByIds(ids);
    }
}