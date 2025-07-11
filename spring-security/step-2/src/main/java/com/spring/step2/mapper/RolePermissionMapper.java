package com.spring.step2.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.RolePermissionDto;
import com.spring.step2.domain.entity.RolePermissionEntity;
import com.spring.step2.domain.vo.RolePermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * * 分页查询角色权限关联表内容
     *
     * @param pageParams 角色权限关联表分页参数
     * @param dto        角色权限关联表查询表单
     * @return 角色权限关联表分页结果
     */
    IPage<RolePermissionVo> selectListByPage(@Param("page") Page<RolePermissionEntity> pageParams, @Param("dto") RolePermissionDto dto);

}
