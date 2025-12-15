package com.spring.step3.service.log.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step3.domain.dto.AuthLogDto;
import com.spring.step3.domain.entity.AuthLogEntity;
import com.spring.step3.domain.vo.AuthLogVo;
import com.spring.step3.domain.vo.result.PageResult;
import com.spring.step3.mapper.AuthLogMapper;
import com.spring.step3.service.log.AuthLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统授权日志表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-19 14:26:58
 */
@DS("testJwt")
@Service
@Transactional
public class AuthLogServiceImpl extends ServiceImpl<AuthLogMapper, AuthLogEntity> implements AuthLogService {

    /**
     * 系统授权日志表 服务实现类
     *
     * @param pageParams 系统授权日志表分页查询page对象
     * @param dto        系统授权日志表分页查询对象
     * @return 查询分页系统授权日志表返回对象
     */
    @Override
    public PageResult<AuthLogVo> getAuthLogPage(Page<AuthLogEntity> pageParams, AuthLogDto dto) {
        IPage<AuthLogVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<AuthLogVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 根据id查询系统授权日志表详情
     *
     * @param id 主键
     * @return 系统授权日志表详情 AuthLogVo}
     */
    public AuthLogVo getAuthLogById(Long id) {
        AuthLogEntity authLogEntity = getById(id);

        AuthLogVo authLogVo = new AuthLogVo();
        BeanUtils.copyProperties(authLogEntity, authLogVo);

        return authLogVo;
    }

    /**
     * 添加系统授权日志表
     *
     * @param dto 系统授权日志表添加
     */
    @Override
    public void addAuthLog(AuthLogDto dto) {
        AuthLogEntity authLog = new AuthLogEntity();
        BeanUtils.copyProperties(dto, authLog);

        save(authLog);
    }

    /**
     * 更新系统授权日志表
     *
     * @param dto 系统授权日志表更新
     */
    @Override
    public void updateAuthLog(AuthLogDto dto) {
        AuthLogEntity authLog = new AuthLogEntity();
        BeanUtils.copyProperties(dto, authLog);

        updateById(authLog);
    }

    /**
     * 删除|批量删除系统授权日志表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteAuthLog(List<Long> ids) {
        removeByIds(ids);
    }
}