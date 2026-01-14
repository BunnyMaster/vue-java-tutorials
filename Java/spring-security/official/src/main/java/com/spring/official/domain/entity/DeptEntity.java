package com.spring.official.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dept")
@Schema(name = "Dept对象", title = "部门表", description = "部门表的实体类对象")
public class DeptEntity {

    @Schema(name = "id", title = "唯一id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "parentId", title = "父级id")
    private Long parentId;

    @Schema(name = "manager", title = "管理者id")
    private String manager;

    @Schema(name = "deptName", title = "部门名称")
    private String deptName;

    @Schema(name = "summary", title = "部门简介")
    private String summary;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除")
    private Boolean isDeleted;

}