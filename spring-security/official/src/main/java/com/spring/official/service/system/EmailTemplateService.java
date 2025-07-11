package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.EmailTemplateDto;
import com.spring.official.domain.entity.EmailTemplateEntity;
import com.spring.official.domain.vo.EmailTemplateVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 邮件模板表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface EmailTemplateService extends IService<EmailTemplateEntity> {

    /**
     * 分页查询邮件模板表
     *
     * @return {@link EmailTemplateVo}
     */
    PageResult<EmailTemplateVo> getEmailTemplatePage(Page<EmailTemplateEntity> pageParams, EmailTemplateDto dto);

    /**
     * 添加邮件模板表
     *
     * @param dto {@link EmailTemplateDto} 添加表单
     */
    void addEmailTemplate(EmailTemplateDto dto);

    /**
     * 更新邮件模板表
     *
     * @param dto {@link EmailTemplateDto} 更新表单
     */
    void updateEmailTemplate(EmailTemplateDto dto);

    /**
     * 删除|批量删除邮件模板表类型
     *
     * @param ids 删除id列表
     */
    void deleteEmailTemplate(List<Long> ids);
}
