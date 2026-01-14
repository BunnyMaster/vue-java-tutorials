package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.UserRoleDto;
import com.spring.official.domain.entity.UserRoleEntity;
import com.spring.official.domain.vo.UserRoleVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统用户角色关系表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 分页查询系统用户角色关系表
     *
     * @return {@link UserRoleVo}
     */
    PageResult<UserRoleVo> getUserRolePage(Page<UserRoleEntity> pageParams, UserRoleDto dto);

    /**
     * 添加系统用户角色关系表
     *
     * @param dto {@link UserRoleDto} 添加表单
     */
    void addUserRole(UserRoleDto dto);

    /**
     * 更新系统用户角色关系表
     *
     * @param dto {@link UserRoleDto} 更新表单
     */
    void updateUserRole(UserRoleDto dto);

    /**
     * 删除|批量删除系统用户角色关系表类型
     *
     * @param ids 删除id列表
     */
    void deleteUserRole(List<Long> ids);
}
