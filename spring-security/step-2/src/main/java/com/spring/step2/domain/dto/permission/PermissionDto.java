package com.spring.step2.domain.dto.permission;

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
@Schema(name = "PermissionDTO对象", title = "系统权限表", description = "系统权限表的DTO对象")
public class PermissionDto {

    @Schema(name = "id", title = "主键ID")
    private Long id;

    @Schema(name = "permissionCode", title = "权限编码")
    private String permissionCode;

    @Schema(name = "url", description = "URL")
    private String url;

    @Schema(name = "method", description = "请求方法类型")
    private String method;
    
    @Schema(name = "description", title = "权限描述")
    private String description;

    @Schema(name = "remark", title = "备注信息")
    private String remark;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户ID")
    private Long createUser;

    @Schema(name = "updateUser", title = "更新用户ID")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除：0-未删除，1-已删除")
    private Boolean isDeleted;

}