package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.MessageDto;
import com.spring.official.domain.entity.MessageEntity;
import com.spring.official.domain.vo.MessageVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.MessageMapper;
import com.spring.official.service.system.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统消息 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

    /**
     * 系统消息 服务实现类
     *
     * @param pageParams 系统消息分页查询page对象
     * @param dto        系统消息分页查询对象
     * @return 查询分页系统消息返回对象
     */
    @Override
    public PageResult<MessageVo> getMessagePage(Page<MessageEntity> pageParams, MessageDto dto) {
        IPage<MessageVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<MessageVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统消息
     *
     * @param dto 系统消息添加
     */
    @Override
    public void addMessage(MessageDto dto) {
        MessageEntity message = new MessageEntity();
        BeanUtils.copyProperties(dto, message);
        save(message);
    }

    /**
     * 更新系统消息
     *
     * @param dto 系统消息更新
     */
    @Override
    public void updateMessage(MessageDto dto) {
        MessageEntity message = new MessageEntity();
        BeanUtils.copyProperties(dto, message);
        updateById(message);
    }

    /**
     * 删除|批量删除系统消息
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteMessage(List<Long> ids) {
        removeByIds(ids);
    }
}