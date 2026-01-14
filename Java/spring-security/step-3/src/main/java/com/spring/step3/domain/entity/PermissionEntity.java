package com.spring.step3.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.step3.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("t_permission")
@Schema(name = "Permission对象", title = "系统权限表", description = "系统权限表的实体类对象")
public class PermissionEntity extends BaseEntity {

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

    @Schema(name = "isDeleted", title = "是否删除：0-未删除，1-已删除")
    @TableField(exist = false)
    private Boolean isDeleted;

}