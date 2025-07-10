package com.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.dto.RouterRoleDto;
import com.spring.domain.entity.RouterRoleEntity;
import com.spring.domain.vo.RouterRoleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 系统路由角色关系表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface RouterRoleMapper extends BaseMapper<RouterRoleEntity> {

    /**
     * * 分页查询系统路由角色关系表内容
     *
     * @param pageParams 系统路由角色关系表分页参数
     * @param dto        系统路由角色关系表查询表单
     * @return 系统路由角色关系表分页结果
     */
    IPage<RouterRoleVo> selectListByPage(@Param("page") Page<RouterRoleEntity> pageParams, @Param("dto") RouterRoleDto dto);

}
