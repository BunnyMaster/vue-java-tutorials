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
@TableName("sys_router_role")
@Schema(name = "RouterRole对象", title = "系统路由角色关系表", description = "系统路由角色关系表的实体类对象")
public class RouterRoleEntity {

    @Schema(name = "id", title = "主键ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "routerId", title = "路由ID")
    private Long routerId;

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "最后修改的时间戳")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否被删除")
    private Boolean isDeleted;

}