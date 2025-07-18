package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.UserDeptDto;
import com.spring.step1.bean.entity.UserDeptEntity;
import com.spring.step1.bean.vo.UserDeptVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 部门用户关系表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface UserDeptMapper extends BaseMapper<UserDeptEntity> {

    /**
     * 分页查询部门用户关系表内容
     *
     * @param pageParams 部门用户关系表分页参数
     * @param dto        部门用户关系表查询表单
     * @return 部门用户关系表分页结果
     */
    IPage<UserDeptVo> selectListByPage(@Param("page") Page<UserDeptEntity> pageParams, @Param("dto") UserDeptDto dto);

}
