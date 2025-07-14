package com.spring.step2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.step2.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("t_role")
@Schema(name = "Role对象", title = "系统角色表", description = "系统角色表的实体类对象")
public class RoleEntity extends BaseEntity {

    @Schema(name = "roleCode", title = "角色码")
    private String roleCode;

    @Schema(name = "description", title = "角色描述")
    private String description;

    @Schema(name = "remark", title = "备注信息")
    private String remark;

    @Schema(name = "isDeleted", title = "是否删除：0-未删除，1-已删除")
    @TableField(exist = false)
    private Boolean isDeleted;

}