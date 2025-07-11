package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.MenuIconDto;
import com.spring.official.domain.entity.MenuIconEntity;
import com.spring.official.domain.vo.MenuIconVo;
import com.spring.official.domain.vo.result.PageResult;

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
