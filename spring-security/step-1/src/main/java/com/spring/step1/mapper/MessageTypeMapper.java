package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.MessageTypeDto;
import com.spring.step1.bean.entity.MessageTypeEntity;
import com.spring.step1.bean.vo.MessageTypeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 系统消息类型 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface MessageTypeMapper extends BaseMapper<MessageTypeEntity> {

    /**
     * 分页查询系统消息类型内容
     *
     * @param pageParams 系统消息类型分页参数
     * @param dto        系统消息类型查询表单
     * @return 系统消息类型分页结果
     */
    IPage<MessageTypeVo> selectListByPage(@Param("page") Page<MessageTypeEntity> pageParams, @Param("dto") MessageTypeDto dto);

}
