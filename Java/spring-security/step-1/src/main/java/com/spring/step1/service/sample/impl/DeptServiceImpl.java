package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.DeptDto;
import com.spring.step1.bean.entity.DeptEntity;
import com.spring.step1.bean.vo.DeptVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.DeptMapper;
import com.spring.step1.service.sample.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    /**
     * 部门表 服务实现类
     *
     * @param pageParams 部门表分页查询page对象
     * @param dto        部门表分页查询对象
     * @return 查询分页部门表返回对象
     */
    @Override
    public PageResult<DeptVo> getDeptPage(Page<DeptEntity> pageParams, DeptDto dto) {
        IPage<DeptVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<DeptVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加部门表
     *
     * @param dto 部门表添加
     */
    @Override
    public void addDept(DeptDto dto) {
        DeptEntity dept = new DeptEntity();
        BeanUtils.copyProperties(dto, dept);
        save(dept);
    }

    /**
     * 更新部门表
     *
     * @param dto 部门表更新
     */
    @Override
    public void updateDept(DeptDto dto) {
        DeptEntity dept = new DeptEntity();
        BeanUtils.copyProperties(dto, dept);
        updateById(dept);
    }

    /**
     * 删除|批量删除部门表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteDept(List<Long> ids) {
        removeByIds(ids);
    }
}