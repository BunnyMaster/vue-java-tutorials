package com.spring.official.domain.entity;

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
@TableName("sys_role")
@Schema(name = "Role对象", title = "系统角色表", description = "系统角色表的实体类对象")
public class RoleEntity {

    @Schema(name = "id", title = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "roleCode", title = "角色代码")
    private String roleCode;

    @Schema(name = "description", title = "描述")
    private String description;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除")
    private Boolean isDeleted;

}