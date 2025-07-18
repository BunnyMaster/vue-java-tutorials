package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.EmailTemplateDto;
import com.spring.step1.bean.entity.EmailTemplateEntity;
import com.spring.step1.bean.vo.EmailTemplateVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 邮件模板表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface EmailTemplateMapper extends BaseMapper<EmailTemplateEntity> {

    /**
     * 分页查询邮件模板表内容
     *
     * @param pageParams 邮件模板表分页参数
     * @param dto        邮件模板表查询表单
     * @return 邮件模板表分页结果
     */
    IPage<EmailTemplateVo> selectListByPage(@Param("page") Page<EmailTemplateEntity> pageParams, @Param("dto") EmailTemplateDto dto);

}
