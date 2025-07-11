package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.UserDeptDto;
import com.spring.official.domain.entity.UserDeptEntity;
import com.spring.official.domain.vo.UserDeptVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 部门用户关系表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface UserDeptService extends IService<UserDeptEntity> {

    /**
     * 分页查询部门用户关系表
     *
     * @return {@link UserDeptVo}
     */
    PageResult<UserDeptVo> getUserDeptPage(Page<UserDeptEntity> pageParams, UserDeptDto dto);

    /**
     * 添加部门用户关系表
     *
     * @param dto {@link UserDeptDto} 添加表单
     */
    void addUserDept(UserDeptDto dto);

    /**
     * 更新部门用户关系表
     *
     * @param dto {@link UserDeptDto} 更新表单
     */
    void updateUserDept(UserDeptDto dto);

    /**
     * 删除|批量删除部门用户关系表类型
     *
     * @param ids 删除id列表
     */
    void deleteUserDept(List<Long> ids);
}
