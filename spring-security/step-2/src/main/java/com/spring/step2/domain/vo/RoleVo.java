package com.spring.step2.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.spring.step2.domain.vo.base.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RoleVO对象", title = "系统角色表", description = "系统角色表的VO对象")
public class RoleVo extends BaseVo {

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

}

