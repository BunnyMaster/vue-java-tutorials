package com.spring.step2.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RoleVO对象", title = "系统角色表", description = "系统角色表的VO对象")
public class RoleVo {

    @Schema(name = "id", title = "主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(name = "roleCode", title = "角色名称")
    private String roleCode;

    @Schema(name = "description", title = "角色描述")
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

