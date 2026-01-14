package com.spring.step2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.step2.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("t_role_permission")
@Schema(name = "RolePermission对象", title = "角色权限关联表", description = "角色权限关联表的实体类对象")
public class RolePermissionEntity extends BaseEntity {

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "permissionId", title = "权限ID")
    private Long permissionId;

    @Schema(name = "isDeleted", title = "是否删除：0-未删除，1-已删除")
    @TableField(exist = false)
    private Boolean isDeleted;

}