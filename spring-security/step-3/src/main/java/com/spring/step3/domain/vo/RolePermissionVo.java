package com.spring.step3.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.spring.step3.domain.vo.base.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RolePermissionVO对象", title = "角色权限关联表", description = "角色权限关联表的VO对象")
public class RolePermissionVo extends BaseVo {

    @Schema(name = "id", title = "主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(name = "roleId", title = "角色ID")
    private Long roleId;

    @Schema(name = "permissionId", title = "权限ID")
    private Long permissionId;

}

