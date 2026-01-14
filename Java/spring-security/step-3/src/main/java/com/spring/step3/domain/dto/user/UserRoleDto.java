package com.spring.step3.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "UserRoleDTO对象", title = "用户角色关联表", description = "用户角色关联表的DTO对象")
public class UserRoleDto {

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "userId", title = "用户ID")
    private Long userId;

}