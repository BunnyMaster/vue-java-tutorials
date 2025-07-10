package com.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.bean.dto.system.RolePermissionDto;
import com.spring.bean.entity.RolePermissionEntity;
import com.spring.bean.vo.RolePermissionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 系统角色权限表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * * 分页查询系统角色权限表内容
     *
     * @param pageParams 系统角色权限表分页参数
     * @param dto        系统角色权限表查询表单
     * @return 系统角色权限表分页结果
     */
    IPage<RolePermissionVo> selectListByPage(@Param("page") Page<RolePermissionEntity> pageParams, @Param("dto") RolePermissionDto dto);

}
