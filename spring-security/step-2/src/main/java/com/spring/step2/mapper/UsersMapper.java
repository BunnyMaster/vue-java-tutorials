package com.spring.step2.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.UsersDto;
import com.spring.step2.domain.entity.UsersEntity;
import com.spring.step2.domain.vo.UsersVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@DS("testJwt")
@Mapper
public interface UsersMapper extends BaseMapper<UsersEntity> {

    /**
     * * 分页查询内容
     *
     * @param pageParams 分页参数
     * @param dto        查询表单
     * @return 分页结果
     */
    IPage<UsersVo> selectListByPage(@Param("page") Page<UsersEntity> pageParams, @Param("dto") UsersDto dto);

}
