package com.spring.step1.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_permission")
@Schema(name = "RolePermission对象", title = "系统角色权限表", description = "系统角色权限表的实体类对象")
public class RolePermissionEntity {

    @Schema(name = "id", title = "ID")
    @TableId(type = IdType.ASSIGN_ID)
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