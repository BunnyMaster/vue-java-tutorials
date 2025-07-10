package com.spring.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_dept")
@Schema(name = "UserDept对象", title = "部门用户关系表", description = "部门用户关系表的实体类对象")
public class UserDeptEntity {

    @Schema(name = "id", title = "ID")
    @TableId(type = IdType.ASSIGN_ID)
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