package com.spring.step3.domain.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "AssignRolePermissionDTO对象", title = "角色分配权限DTO", description = "根据角色id分配权限")
public class AssignRolePermissionDto {

    @Schema(name = "roleId", title = "角色ID")
    @NotNull(message = "角色id为空")
    private Long roleId;

    @Schema(name = "permissionId", title = "权限ID")
    @NotEmpty(message = "权限id为空")
    private List<Long> permissionIds;

}
