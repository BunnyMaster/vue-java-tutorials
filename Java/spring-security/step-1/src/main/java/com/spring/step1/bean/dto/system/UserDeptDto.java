package com.spring.step1.bean.dto.system;

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
@Schema(name = "UserDeptDTO对象", title = "部门用户关系表", description = "部门用户关系表的DTO对象")
public class UserDeptDto {

    @Schema(name = "id", title = "ID")
    private Long id;

    @Schema(name = "userId", title = "用户id")
    private Long userId;

    @Schema(name = "deptId", title = "部门id")
    private Long deptId;

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