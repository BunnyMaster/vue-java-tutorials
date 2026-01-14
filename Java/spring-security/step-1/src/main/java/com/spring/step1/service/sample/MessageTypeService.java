package com.spring.step1.service.sample;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step1.bean.dto.system.MessageTypeDto;
import com.spring.step1.bean.entity.MessageTypeEntity;
import com.spring.step1.bean.vo.MessageTypeVo;
import com.spring.step1.bean.vo.result.PageResult;

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
