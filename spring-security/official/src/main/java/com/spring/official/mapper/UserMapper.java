package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.UserDto;
import com.spring.official.domain.entity.UserEntity;
import com.spring.official.domain.vo.UserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 分页查询用户信息内容
     *
     * @param pageParams 用户信息分页参数
     * @param dto        用户信息查询表单
     * @return 用户信息分页结果
     */
    IPage<UserVo> selectListByPage(@Param("page") Page<UserEntity> pageParams, @Param("dto") UserDto dto);

}
