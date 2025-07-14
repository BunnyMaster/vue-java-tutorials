package com.spring.step2.domain.dto.user;

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
@Schema(name = "AssignUserRoleDTO对象", title = "用户分配角色DTO", description = "根据用户id分配用户角色")
public class AssignUserRoleDto {

    @Schema(name = "userId", title = "用户ID")
    @NotNull(message = "用户id为空")
    private Long userId;

    @Schema(name = "roleId", title = "角色ID")
    @NotEmpty(message = "角色ID为空")
    private List<Long> roleIds;

}
