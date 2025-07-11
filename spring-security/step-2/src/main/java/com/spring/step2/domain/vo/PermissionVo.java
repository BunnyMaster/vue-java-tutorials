package com.spring.step2.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PermissionVO对象", title = "系统权限表", description = "系统权限表的VO对象")
public class PermissionVo {

    @Schema(name = "id", title = "主键ID")
    private Long id;

    @Schema(name = "permissionCode", title = "权限编码")
    private String permissionCode;

    @Schema(name = "description", title = "权限描述")
    private String description;

    @Schema(name = "remark", title = "备注信息")
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

