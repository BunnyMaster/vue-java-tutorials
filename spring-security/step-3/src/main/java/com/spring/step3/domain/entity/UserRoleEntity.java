package com.spring.step3.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.step3.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_role")
@Schema(name = "UserRole对象", title = "用户角色关联表", description = "用户角色关联表的实体类对象")
public class UserRoleEntity extends BaseEntity {

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "userId", title = "用户ID")
    private Long userId;

    @Schema(name = "isDeleted", title = "是否删除：0-未删除，1-已删除")
    @TableField(exist = false)
    private Boolean isDeleted;

}