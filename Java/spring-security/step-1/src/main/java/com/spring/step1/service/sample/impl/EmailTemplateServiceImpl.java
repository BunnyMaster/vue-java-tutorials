package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.EmailTemplateDto;
import com.spring.step1.bean.entity.EmailTemplateEntity;
import com.spring.step1.bean.vo.EmailTemplateVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.EmailTemplateMapper;
import com.spring.step1.service.sample.EmailTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 邮件模板表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class EmailTemplateServiceImpl extends ServiceImpl<EmailTemplateMapper, EmailTemplateEntity> implements EmailTemplateService {

    /**
     * 邮件模板表 服务实现类
     *
     * @param pageParams 邮件模板表分页查询page对象
     * @param dto        邮件模板表分页查询对象
     * @return 查询分页邮件模板表返回对象
     */
    @Override
    public PageResult<EmailTemplateVo> getEmailTemplatePage(Page<EmailTemplateEntity> pageParams, EmailTemplateDto dto) {
        IPage<EmailTemplateVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<EmailTemplateVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加邮件模板表
     *
     * @param dto 邮件模板表添加
     */
    @Override
    public void addEmailTemplate(EmailTemplateDto dto) {
        EmailTemplateEntity emailTemplate = new EmailTemplateEntity();
        BeanUtils.copyProperties(dto, emailTemplate);
        save(emailTemplate);
    }

    /**
     * 更新邮件模板表
     *
     * @param dto 邮件模板表更新
     */
    @Override
    public void updateEmailTemplate(EmailTemplateDto dto) {
        EmailTemplateEntity emailTemplate = new EmailTemplateEntity();
        BeanUtils.copyProperties(dto, emailTemplate);
        updateById(emailTemplate);
    }

    /**
     * 删除|批量删除邮件模板表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteEmailTemplate(List<Long> ids) {
        removeByIds(ids);
    }
}