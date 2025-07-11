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
@Schema(name = "RolePermissionDTO对象", title = "系统角色权限表", description = "系统角色权限表的DTO对象")
public class RolePermissionDto {

    @Schema(name = "id", title = "ID")
    private Long id;

    @Schema(name = "roleId", title = "角色id")
    private Long roleId;

    @Schema(name = "powerId", title = "权限id")
    private Long powerId;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "更新用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除，0-未删除，1-已删除")
    private Boolean isDeleted;

}