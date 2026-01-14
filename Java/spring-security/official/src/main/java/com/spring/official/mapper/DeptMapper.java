package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.DeptDto;
import com.spring.official.domain.entity.DeptEntity;
import com.spring.official.domain.vo.DeptVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface DeptMapper extends BaseMapper<DeptEntity> {

    /**
     * 分页查询部门表内容
     *
     * @param pageParams 部门表分页参数
     * @param dto        部门表查询表单
     * @return 部门表分页结果
     */
    IPage<DeptVo> selectListByPage(@Param("page") Page<DeptEntity> pageParams, @Param("dto") DeptDto dto);

}
