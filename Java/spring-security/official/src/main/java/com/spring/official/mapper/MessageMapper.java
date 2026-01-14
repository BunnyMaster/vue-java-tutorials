package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.MessageDto;
import com.spring.official.domain.entity.MessageEntity;
import com.spring.official.domain.vo.MessageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 系统消息 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {

    /**
     * 分页查询系统消息内容
     *
     * @param pageParams 系统消息分页参数
     * @param dto        系统消息查询表单
     * @return 系统消息分页结果
     */
    IPage<MessageVo> selectListByPage(@Param("page") Page<MessageEntity> pageParams, @Param("dto") MessageDto dto);

}
