package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.I18nDto;
import com.spring.official.domain.entity.I18nEntity;
import com.spring.official.domain.vo.I18nVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 多语言表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface I18nMapper extends BaseMapper<I18nEntity> {

    /**
     * 分页查询多语言表内容
     *
     * @param pageParams 多语言表分页参数
     * @param dto        多语言表查询表单
     * @return 多语言表分页结果
     */
    IPage<I18nVo> selectListByPage(@Param("page") Page<I18nEntity> pageParams, @Param("dto") I18nDto dto);

}
