package com.spring.official.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.spring.official.domain.dto.system.FilesPartDetailDto;
import com.spring.official.domain.entity.FilesPartDetailEntity;
import com.spring.official.domain.vo.FilesPartDetailVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 文件分片信息表，仅在手动分片上传时使用 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Mapper
public interface FilesPartDetailMapper extends BaseMapper<FilesPartDetailEntity> {

    /**
     * 分页查询文件分片信息表，仅在手动分片上传时使用内容
     *
     * @param pageParams 文件分片信息表，仅在手动分片上传时使用分页参数
     * @param dto        文件分片信息表，仅在手动分片上传时使用查询表单
     * @return 文件分片信息表，仅在手动分片上传时使用分页结果
     */
    IPage<FilesPartDetailVo> selectListByPage(@Param("page") Page<FilesPartDetailEntity> pageParams, @Param("dto") FilesPartDetailDto dto);

}
