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
@Schema(name = "PermissionVO对象", title = "系统权限表", description = "系统权限表的VO对象")
public class PermissionVo extends BaseVo {

    @Schema(name = "id", title = "主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(name = "permissionCode", title = "权限编码")
    private String permissionCode;

    @Schema(name = "url", description = "URL")
    private String url;

    @Schema(name = "method", description = "请求方法类型")
    private String method;

    @Schema(name = "description", title = "权限描述")
    private String description;

    @Schema(name = "remark", title = "备注信息")
    private String remark;

}

