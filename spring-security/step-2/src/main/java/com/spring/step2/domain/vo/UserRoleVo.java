package com.spring.step2.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserRoleVO对象", title = "用户角色关联表", description = "用户角色关联表的VO对象")
public class UserRoleVo {

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "userId", title = "用户ID")
    private Long userId;

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

