package com.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.dto.RoleDto;
import com.spring.domain.entity.RoleEntity;
import com.spring.domain.vo.RoleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     * * 分页查询系统角色表内容
     *
     * @param pageParams 系统角色表分页参数
     * @param dto        系统角色表查询表单
     * @return 系统角色表分页结果
     */
    IPage<RoleVo> selectListByPage(@Param("page") Page<RoleEntity> pageParams, @Param("dto") RoleDto dto);

}
