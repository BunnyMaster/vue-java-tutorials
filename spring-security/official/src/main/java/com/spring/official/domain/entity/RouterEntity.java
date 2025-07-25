package com.spring.official.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_router")
@Schema(name = "Router对象", title = "系统菜单表", description = "系统菜单表的实体类对象")
public class RouterEntity {

    @Schema(name = "id", title = "主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "parentId", title = "父级id")
    private Long parentId;

    @Schema(name = "path", title = "在项目中路径")
    private String path;

    @Schema(name = "routeName", title = "路由名称")
    private String routeName;

    @Schema(name = "component", title = "组件位置")
    private String component;

    @Schema(name = "redirect", title = "路由重定向")
    private String redirect;

    @Schema(name = "menuType", title = "菜单类型")
    private Integer menuType;

    @Schema(name = "meta", title = "路由meta")
    private String meta;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "updateTime", title = "记录文件最后修改的时间戳")
    private LocalDateTime updateTime;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "isDeleted", title = "文件是否被删除")
    private Boolean isDeleted;

}