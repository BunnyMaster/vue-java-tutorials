package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.DeptDto;
import com.spring.domain.entity.DeptEntity;
import com.spring.domain.vo.DeptVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface DeptService extends IService<DeptEntity> {

    /**
     * 分页查询部门表
     *
     * @return {@link DeptVo}
     */
    PageResult<DeptVo> getDeptPage(Page<DeptEntity> pageParams, DeptDto dto);

    /**
     * 添加部门表
     *
     * @param dto {@link DeptDto} 添加表单
     */
    void addDept(DeptDto dto);

    /**
     * 更新部门表
     *
     * @param dto {@link DeptDto} 更新表单
     */
    void updateDept(DeptDto dto);

    /**
     * 删除|批量删除部门表类型
     *
     * @param ids 删除id列表
     */
    void deleteDept(List<Long> ids);
}
