package com.spring.step2.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.permission.PermissionDto;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.vo.PermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    /**
     * 分页查询系统权限表内容
     *
     * @param pageParams 系统权限表分页参数
     * @param dto        系统权限表查询表单
     * @return 系统权限表分页结果
     */
    IPage<PermissionVo> selectListByPage(@Param("page") Page<PermissionEntity> pageParams, @Param("dto") PermissionDto dto);

}
