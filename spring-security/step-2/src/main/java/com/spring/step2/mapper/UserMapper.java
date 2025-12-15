package com.spring.step2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.user.UserDto;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.entity.RoleEntity;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户基本信息表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 分页查询用户基本信息表内容
     *
     * @param pageParams 用户基本信息表分页参数
     * @param dto        用户基本信息表查询表单
     * @return 用户基本信息表分页结果
     */
    IPage<UserVo> selectListByPage(@Param("page") Page<UserEntity> pageParams, @Param("dto") UserDto dto);

    /**
     * 根据用户id查找当前用户的权限
     *
     * @param userId 用户id
     * @return 权限列表
     */
    List<PermissionEntity> selectPermissionByUserId(Long userId);

    /**
     * 根据用户名查询当前用户
     *
     * @param username 用户名
     * @return 用户 {@link UserEntity}
     */
    UserEntity selectByUsername(String username);

    /**
     * 根据用户id查找该用户的角色内容
     *
     * @param userId 用户id
     * @return 当前用户的角色信息
     */
    List<RoleEntity> selectRolesByUserId(Long userId);
}
