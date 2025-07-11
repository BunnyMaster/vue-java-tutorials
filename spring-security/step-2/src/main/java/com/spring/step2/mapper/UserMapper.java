package com.spring.step2.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step2.domain.dto.UserDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@DS("testJwt")
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * * 分页查询用户内容
     *
     * @param pageParams 用户分页参数
     * @param dto        用户查询表单
     * @return 用户分页结果
     */
    IPage<UserVo> selectListByPage(@Param("page") Page<UserEntity> pageParams, @Param("dto") UserDto dto);

}
