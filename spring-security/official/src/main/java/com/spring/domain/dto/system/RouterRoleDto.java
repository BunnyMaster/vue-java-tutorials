package com.spring.domain.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "RouterRoleDTO对象", title = "系统路由角色关系表", description = "系统路由角色关系表的DTO对象")
public class RouterRoleDto {

    @Schema(name = "id", title = "主键ID")
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