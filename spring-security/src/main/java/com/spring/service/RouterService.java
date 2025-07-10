package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.RouterDto;
import com.spring.domain.entity.RouterEntity;
import com.spring.domain.vo.RouterVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface RouterService extends IService<RouterEntity> {

    /**
     * 分页查询系统菜单表
     *
     * @return {@link RouterVo}
     */
    PageResult<RouterVo> getRouterPage(Page<RouterEntity> pageParams, RouterDto dto);

    /**
     * 添加系统菜单表
     *
     * @param dto {@link RouterDto} 添加表单
     */
    void addRouter(RouterDto dto);

    /**
     * 更新系统菜单表
     *
     * @param dto {@link RouterDto} 更新表单
     */
    void updateRouter(RouterDto dto);

    /**
     * 删除|批量删除系统菜单表类型
     *
     * @param ids 删除id列表
     */
    void deleteRouter(List<Long> ids);
}
