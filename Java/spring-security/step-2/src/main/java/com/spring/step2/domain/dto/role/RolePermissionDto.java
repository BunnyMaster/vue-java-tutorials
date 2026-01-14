package com.spring.step2.domain.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "RolePermissionDTO对象", title = "角色权限关联表", description = "角色权限关联表的DTO对象")
public class RolePermissionDto {

    @Schema(name = "id", title = "主键ID")
    private Long id;

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "permissionId", title = "权限ID")
    private Long permissionId;

}