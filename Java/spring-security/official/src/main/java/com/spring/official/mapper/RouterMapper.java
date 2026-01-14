package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.RouterDto;
import com.spring.official.domain.entity.RouterEntity;
import com.spring.official.domain.vo.RouterVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface RouterMapper extends BaseMapper<RouterEntity> {

    /**
     * 分页查询系统菜单表内容
     *
     * @param pageParams 系统菜单表分页参数
     * @param dto        系统菜单表查询表单
     * @return 系统菜单表分页结果
     */
    IPage<RouterVo> selectListByPage(@Param("page") Page<RouterEntity> pageParams, @Param("dto") RouterDto dto);

}
