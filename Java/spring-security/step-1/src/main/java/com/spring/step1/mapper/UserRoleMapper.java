package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.UserRoleDto;
import com.spring.step1.bean.entity.UserRoleEntity;
import com.spring.step1.bean.vo.UserRoleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 系统用户角色关系表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    /**
     * 分页查询系统用户角色关系表内容
     *
     * @param pageParams 系统用户角色关系表分页参数
     * @param dto        系统用户角色关系表查询表单
     * @return 系统用户角色关系表分页结果
     */
    IPage<UserRoleVo> selectListByPage(@Param("page") Page<UserRoleEntity> pageParams, @Param("dto") UserRoleDto dto);

}
