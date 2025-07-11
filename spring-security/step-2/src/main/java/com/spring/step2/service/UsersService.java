package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.UsersDto;
import com.spring.step2.domain.entity.UsersEntity;
import com.spring.step2.domain.vo.UsersVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
public interface UsersService extends IService<UsersEntity> {

    /**
     * 分页查询
     *
     * @return {@link UsersVo}
     */
    PageResult<UsersVo> getUsersPage(Page<UsersEntity> pageParams, UsersDto dto);

    /**
     * 添加
     *
     * @param dto {@link UsersDto} 添加表单
     */
    void addUsers(UsersDto dto);

    /**
     * 更新
     *
     * @param dto {@link UsersDto} 更新表单
     */
    void updateUsers(UsersDto dto);

    /**
     * 删除|批量删除类型
     *
     * @param ids 删除id列表
     */
    void deleteUsers(List<Long> ids);
}
