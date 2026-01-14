package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.FilesDto;
import com.spring.official.domain.entity.FilesEntity;
import com.spring.official.domain.vo.FilesVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 文件记录 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface FilesMapper extends BaseMapper<FilesEntity> {

    /**
     * 分页查询文件记录内容
     *
     * @param pageParams 文件记录分页参数
     * @param dto        文件记录查询表单
     * @return 文件记录分页结果
     */
    IPage<FilesVo> selectListByPage(@Param("page") Page<FilesEntity> pageParams, @Param("dto") FilesDto dto);

}
