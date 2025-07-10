package com.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.dto.MessageTypeDto;
import com.spring.domain.entity.MessageTypeEntity;
import com.spring.domain.vo.MessageTypeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

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
     * * 分页查询系统消息类型内容
     *
     * @param pageParams 系统消息类型分页参数
     * @param dto        系统消息类型查询表单
     * @return 系统消息类型分页结果
     */
    IPage<MessageTypeVo> selectListByPage(@Param("page") Page<MessageTypeEntity> pageParams, @Param("dto") MessageTypeDto dto);

}
