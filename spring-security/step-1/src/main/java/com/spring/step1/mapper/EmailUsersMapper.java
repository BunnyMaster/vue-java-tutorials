package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.EmailUsersDto;
import com.spring.step1.bean.entity.EmailUsersEntity;
import com.spring.step1.bean.vo.EmailUsersVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 邮箱发送表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface EmailUsersMapper extends BaseMapper<EmailUsersEntity> {

    /**
     * 分页查询邮箱发送表内容
     *
     * @param pageParams 邮箱发送表分页参数
     * @param dto        邮箱发送表查询表单
     * @return 邮箱发送表分页结果
     */
    IPage<EmailUsersVo> selectListByPage(@Param("page") Page<EmailUsersEntity> pageParams, @Param("dto") EmailUsersDto dto);

}
