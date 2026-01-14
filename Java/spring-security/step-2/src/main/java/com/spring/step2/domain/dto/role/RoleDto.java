package com.spring.step2.domain.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "RoleDTO对象", title = "系统角色表", description = "系统角色表的DTO对象")
public class RoleDto {

    @Schema(name = "id", title = "主键ID")
    private Long id;

    @Schema(name = "roleCode", title = "角色码")
    @NotBlank(message = "角色码不能为空")
    private String roleCode;

    @Schema(name = "description", title = "角色描述")
    @NotBlank(message = "角色描述不能为空")
    private String description;

    @Schema(name = "remark", title = "备注信息")
    @NotBlank(message = "备注信息不能为空")
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