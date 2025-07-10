package com.spring.service.impl;

import com.spring.mapper.MessageReceivedMapper;
import com.spring.service.MessageReceivedService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.service.MessageReceivedService;
import com.spring.domain.dto.MessageReceivedDto;
import com.spring.domain.entity.MessageReceivedEntity;
import com.spring.domain.vo.MessageReceivedVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class MessageReceivedServiceImpl extends ServiceImpl<MessageReceivedMapper, MessageReceivedEntity> implements MessageReceivedService {

    /**
     * *  服务实现类
     *
     * @param pageParams 分页查询page对象
     * @param dto        分页查询对象
     * @return 查询分页返回对象
     */
    @Override
    public PageResult<MessageReceivedVo> getMessageReceivedPage(Page<MessageReceivedEntity> pageParams, MessageReceivedDto dto) {
        IPage<MessageReceivedVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<MessageReceivedVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加
     *
     * @param dto 添加
     */
    @Override
    public void addMessageReceived(MessageReceivedDto dto) {
            MessageReceivedEntity messageReceived = new MessageReceivedEntity();
        BeanUtils.copyProperties(dto, messageReceived);
        save(messageReceived);
    }

    /**
     * 更新
     *
     * @param dto 更新
     */
    @Override
    public void updateMessageReceived(MessageReceivedDto dto) {
            MessageReceivedEntity messageReceived = new MessageReceivedEntity();
        BeanUtils.copyProperties(dto, messageReceived);
        updateById(messageReceived);
    }

    /**
     * 删除|批量删除
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteMessageReceived(List<Long> ids) {
        removeByIds(ids);
    }
}