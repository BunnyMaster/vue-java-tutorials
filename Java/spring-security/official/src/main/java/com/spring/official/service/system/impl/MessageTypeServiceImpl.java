package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.MessageTypeDto;
import com.spring.official.domain.entity.MessageTypeEntity;
import com.spring.official.domain.vo.MessageTypeVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.MessageTypeMapper;
import com.spring.official.service.system.MessageTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统消息类型 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class MessageTypeServiceImpl extends ServiceImpl<MessageTypeMapper, MessageTypeEntity> implements MessageTypeService {

    /**
     * 系统消息类型 服务实现类
     *
     * @param pageParams 系统消息类型分页查询page对象
     * @param dto        系统消息类型分页查询对象
     * @return 查询分页系统消息类型返回对象
     */
    @Override
    public PageResult<MessageTypeVo> getMessageTypePage(Page<MessageTypeEntity> pageParams, MessageTypeDto dto) {
        IPage<MessageTypeVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<MessageTypeVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统消息类型
     *
     * @param dto 系统消息类型添加
     */
    @Override
    public void addMessageType(MessageTypeDto dto) {
        MessageTypeEntity messageType = new MessageTypeEntity();
        BeanUtils.copyProperties(dto, messageType);
        save(messageType);
    }

    /**
     * 更新系统消息类型
     *
     * @param dto 系统消息类型更新
     */
    @Override
    public void updateMessageType(MessageTypeDto dto) {
        MessageTypeEntity messageType = new MessageTypeEntity();
        BeanUtils.copyProperties(dto, messageType);
        updateById(messageType);
    }

    /**
     * 删除|批量删除系统消息类型
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteMessageType(List<Long> ids) {
        removeByIds(ids);
    }
}