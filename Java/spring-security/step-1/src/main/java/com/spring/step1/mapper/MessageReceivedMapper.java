package com.spring.step1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.step1.bean.dto.system.MessageReceivedDto;
import com.spring.step1.bean.entity.MessageReceivedEntity;
import com.spring.step1.bean.vo.MessageReceivedVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface MessageReceivedMapper extends BaseMapper<MessageReceivedEntity> {

    /**
     * 分页查询内容
     *
     * @param pageParams 分页参数
     * @param dto        查询表单
     * @return 分页结果
     */
    IPage<MessageReceivedVo> selectListByPage(@Param("page") Page<MessageReceivedEntity> pageParams, @Param("dto") MessageReceivedDto dto);

}
