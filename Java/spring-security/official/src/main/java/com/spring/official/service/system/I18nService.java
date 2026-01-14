package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.I18nDto;
import com.spring.official.domain.entity.I18nEntity;
import com.spring.official.domain.vo.I18nVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 多语言表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface I18nService extends IService<I18nEntity> {

    /**
     * 分页查询多语言表
     *
     * @return {@link I18nVo}
     */
    PageResult<I18nVo> getI18nPage(Page<I18nEntity> pageParams, I18nDto dto);

    /**
     * 添加多语言表
     *
     * @param dto {@link I18nDto} 添加表单
     */
    void addI18n(I18nDto dto);

    /**
     * 更新多语言表
     *
     * @param dto {@link I18nDto} 更新表单
     */
    void updateI18n(I18nDto dto);

    /**
     * 删除|批量删除多语言表类型
     *
     * @param ids 删除id列表
     */
    void deleteI18n(List<Long> ids);
}
