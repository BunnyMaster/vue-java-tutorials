package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.MenuIconDto;
import com.spring.domain.entity.MenuIconEntity;
import com.spring.domain.vo.MenuIconVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 图标code不能重复 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:28
 */
public interface MenuIconService extends IService<MenuIconEntity> {

    /**
     * 分页查询图标code不能重复
     *
     * @return {@link MenuIconVo}
     */
    PageResult<MenuIconVo> getMenuIconPage(Page<MenuIconEntity> pageParams, MenuIconDto dto);

    /**
     * 添加图标code不能重复
     *
     * @param dto {@link MenuIconDto} 添加表单
     */
    void addMenuIcon(MenuIconDto dto);

    /**
     * 更新图标code不能重复
     *
     * @param dto {@link MenuIconDto} 更新表单
     */
    void updateMenuIcon(MenuIconDto dto);

    /**
     * 删除|批量删除图标code不能重复类型
     *
     * @param ids 删除id列表
     */
    void deleteMenuIcon(List<Long> ids);
}
