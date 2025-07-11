package com.spring.step2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.UserRoleDto;
import com.spring.step2.domain.entity.UserRoleEntity;
import com.spring.step2.domain.vo.UserRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    /**
     * * 分页查询用户角色关联表内容
     *
     * @param pageParams 用户角色关联表分页参数
     * @param dto        用户角色关联表查询表单
     * @return 用户角色关联表分页结果
     */
    IPage<UserRoleVo> selectListByPage(@Param("page") Page<UserRoleEntity> pageParams, @Param("dto") UserRoleDto dto);

}
