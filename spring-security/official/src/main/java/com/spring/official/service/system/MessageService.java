package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.MessageDto;
import com.spring.official.domain.entity.MessageEntity;
import com.spring.official.domain.vo.MessageVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统消息 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface MessageService extends IService<MessageEntity> {

    /**
     * 分页查询系统消息
     *
     * @return {@link MessageVo}
     */
    PageResult<MessageVo> getMessagePage(Page<MessageEntity> pageParams, MessageDto dto);

    /**
     * 添加系统消息
     *
     * @param dto {@link MessageDto} 添加表单
     */
    void addMessage(MessageDto dto);

    /**
     * 更新系统消息
     *
     * @param dto {@link MessageDto} 更新表单
     */
    void updateMessage(MessageDto dto);

    /**
     * 删除|批量删除系统消息类型
     *
     * @param ids 删除id列表
     */
    void deleteMessage(List<Long> ids);
}
