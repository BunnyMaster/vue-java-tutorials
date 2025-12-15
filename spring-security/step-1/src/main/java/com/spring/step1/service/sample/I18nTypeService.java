package com.spring.step1.service.sample;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step1.bean.dto.system.I18nTypeDto;
import com.spring.step1.bean.entity.I18nTypeEntity;
import com.spring.step1.bean.vo.I18nTypeVo;
import com.spring.step1.bean.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 多语言类型表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface I18nTypeService extends IService<I18nTypeEntity> {

    /**
     * 分页查询多语言类型表
     *
     * @return {@link I18nTypeVo}
     */
    PageResult<I18nTypeVo> getI18nTypePage(Page<I18nTypeEntity> pageParams, I18nTypeDto dto);

    /**
     * 添加多语言类型表
     *
     * @param dto {@link I18nTypeDto} 添加表单
     */
    void addI18nType(I18nTypeDto dto);

    /**
     * 更新多语言类型表
     *
     * @param dto {@link I18nTypeDto} 更新表单
     */
    void updateI18nType(I18nTypeDto dto);

    /**
     * 删除|批量删除多语言类型表类型
     *
     * @param ids 删除id列表
     */
    void deleteI18nType(List<Long> ids);
}
