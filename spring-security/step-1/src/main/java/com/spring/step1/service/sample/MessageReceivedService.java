package com.spring.step1.service.sample;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step1.bean.dto.system.MessageReceivedDto;
import com.spring.step1.bean.entity.MessageReceivedEntity;
import com.spring.step1.bean.vo.MessageReceivedVo;
import com.spring.step1.bean.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface MessageReceivedService extends IService<MessageReceivedEntity> {

    /**
     * 分页查询
     *
     * @return {@link MessageReceivedVo}
     */
    PageResult<MessageReceivedVo> getMessageReceivedPage(Page<MessageReceivedEntity> pageParams, MessageReceivedDto dto);

    /**
     * 添加
     *
     * @param dto {@link MessageReceivedDto} 添加表单
     */
    void addMessageReceived(MessageReceivedDto dto);

    /**
     * 更新
     *
     * @param dto {@link MessageReceivedDto} 更新表单
     */
    void updateMessageReceived(MessageReceivedDto dto);

    /**
     * 删除|批量删除类型
     *
     * @param ids 删除id列表
     */
    void deleteMessageReceived(List<Long> ids);
}
