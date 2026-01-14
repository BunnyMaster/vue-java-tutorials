package com.spring.step3.service.log;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step3.domain.dto.AuthLogDto;
import com.spring.step3.domain.entity.AuthLogEntity;
import com.spring.step3.domain.vo.AuthLogVo;
import com.spring.step3.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统授权日志表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-19 14:26:58
 */
public interface AuthLogService extends IService<AuthLogEntity> {

    /**
     * 分页查询系统授权日志表
     *
     * @return 系统授权日志表分页结果 {@link AuthLogVo}
     */
    PageResult<AuthLogVo> getAuthLogPage(Page<AuthLogEntity> pageParams, AuthLogDto dto);

    /**
     * 根据id查询系统授权日志表详情
     *
     * @param id 主键
     * @return 系统授权日志表详情 AuthLogVo}
     */
    AuthLogVo getAuthLogById(Long id);

    /**
     * 添加系统授权日志表
     *
     * @param dto {@link AuthLogDto} 添加表单
     */
    void addAuthLog(AuthLogDto dto);

    /**
     * 更新系统授权日志表
     *
     * @param dto {@link AuthLogDto} 更新表单
     */
    void updateAuthLog(AuthLogDto dto);

    /**
     * 删除|批量删除系统授权日志表类型
     *
     * @param ids 删除id列表
     */
    void deleteAuthLog(List<Long> ids);
}
