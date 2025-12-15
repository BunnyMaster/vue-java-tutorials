package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.MessageTypeDto;
import com.spring.official.domain.entity.MessageTypeEntity;
import com.spring.official.domain.vo.MessageTypeVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统消息类型 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface MessageTypeService extends IService<MessageTypeEntity> {

    /**
     * 分页查询系统消息类型
     *
     * @return {@link MessageTypeVo}
     */
    PageResult<MessageTypeVo> getMessageTypePage(Page<MessageTypeEntity> pageParams, MessageTypeDto dto);

    /**
     * 添加系统消息类型
     *
     * @param dto {@link MessageTypeDto} 添加表单
     */
    void addMessageType(MessageTypeDto dto);

    /**
     * 更新系统消息类型
     *
     * @param dto {@link MessageTypeDto} 更新表单
     */
    void updateMessageType(MessageTypeDto dto);

    /**
     * 删除|批量删除系统消息类型类型
     *
     * @param ids 删除id列表
     */
    void deleteMessageType(List<Long> ids);
}
