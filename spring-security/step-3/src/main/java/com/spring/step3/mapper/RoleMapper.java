package com.spring.step3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step3.domain.dto.role.RoleDto;
import com.spring.step3.domain.entity.RoleEntity;
import com.spring.step3.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     * 分页查询系统角色表内容
     *
     * @param pageParams 系统角色表分页参数
     * @param dto        系统角色表查询表单
     * @return 系统角色表分页结果
     */
    IPage<RoleVo> selectListByPage(@Param("page") Page<RoleEntity> pageParams, @Param("dto") RoleDto dto);

}
