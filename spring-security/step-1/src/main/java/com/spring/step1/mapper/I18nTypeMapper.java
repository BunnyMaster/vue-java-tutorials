package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.I18nTypeDto;
import com.spring.step1.bean.entity.I18nTypeEntity;
import com.spring.step1.bean.vo.I18nTypeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 多语言类型表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface I18nTypeMapper extends BaseMapper<I18nTypeEntity> {

    /**
     * 分页查询多语言类型表内容
     *
     * @param pageParams 多语言类型表分页参数
     * @param dto        多语言类型表查询表单
     * @return 多语言类型表分页结果
     */
    IPage<I18nTypeVo> selectListByPage(@Param("page") Page<I18nTypeEntity> pageParams, @Param("dto") I18nTypeDto dto);

}
