package com.spring.step1.bean.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "I18nTypeDTO对象", title = "多语言类型表", description = "多语言类型表的DTO对象")
public class I18nTypeDto {

    @Schema(name = "id", title = "主键id")
    private Long id;

    @Schema(name = "typeName", title = "多语言类型(比如zh,en)")
    private String typeName;

    @Schema(name = "summary", title = "名称解释(比如中文,英文)")
    private String summary;

    @Schema(name = "isDefault", title = "是否为默认语言")
    private Integer isDefault;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "updateTime", title = "记录文件最后修改的时间戳")
    private LocalDateTime updateTime;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "isDeleted", title = "文件是否被删除")
    private Boolean isDeleted;

}