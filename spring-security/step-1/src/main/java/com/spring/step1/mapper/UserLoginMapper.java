package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.UserLoginDto;
import com.spring.step1.bean.entity.UserLoginEntity;
import com.spring.step1.bean.vo.UserLoginVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 用户登录日志 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface UserLoginMapper extends BaseMapper<UserLoginEntity> {

    /**
     * 分页查询用户登录日志内容
     *
     * @param pageParams 用户登录日志分页参数
     * @param dto        用户登录日志查询表单
     * @return 用户登录日志分页结果
     */
    IPage<UserLoginVo> selectListByPage(@Param("page") Page<UserLoginEntity> pageParams, @Param("dto") UserLoginDto dto);

}
