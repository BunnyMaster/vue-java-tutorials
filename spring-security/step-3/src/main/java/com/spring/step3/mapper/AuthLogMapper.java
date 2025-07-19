package com.spring.step3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step3.domain.dto.AuthLogDto;
import com.spring.step3.domain.entity.AuthLogEntity;
import com.spring.step3.domain.vo.AuthLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统授权日志表 Mapper 接口
 * </p>
 *
 * @author AuthoritySystem
 * @since 2025-07-19 14:26:58
 */
@Mapper
public interface AuthLogMapper extends BaseMapper<AuthLogEntity> {

    /**
     * 分页查询系统授权日志表内容
     *
     * @param pageParams 系统授权日志表分页参数
     * @param dto        系统授权日志表查询表单
     * @return 系统授权日志表分页结果
     */
    IPage<AuthLogVo> selectListByPage(@Param("page") Page<AuthLogEntity> pageParams, @Param("dto") AuthLogDto dto);

}
