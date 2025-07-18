package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.MenuIconDto;
import com.spring.official.domain.entity.MenuIconEntity;
import com.spring.official.domain.vo.MenuIconVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 图标code不能重复 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:28
 */
@Mapper
public interface MenuIconMapper extends BaseMapper<MenuIconEntity> {

    /**
     * 分页查询图标code不能重复内容
     *
     * @param pageParams 图标code不能重复分页参数
     * @param dto        图标code不能重复查询表单
     * @return 图标code不能重复分页结果
     */
    IPage<MenuIconVo> selectListByPage(@Param("page") Page<MenuIconEntity> pageParams, @Param("dto") MenuIconDto dto);

}
