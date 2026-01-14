package com.spring.official.domain.dto.system;

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
@Schema(name = "MenuIconDTO对象", title = "图标code不能重复", description = "图标code不能重复的DTO对象")
public class MenuIconDto {

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "iconCode", title = "icon类名")
    private String iconCode;

    @Schema(name = "iconName", title = "icon 名称")
    private String iconName;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除")
    private Integer isDeleted;

}