package com.spring.step1.bean.entity;

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
@TableName("sys_user")
@Schema(name = "User对象", title = "用户信息", description = "用户信息的实体类对象")
public class UserEntity {

    @Schema(name = "id", title = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "nickname", title = "昵称")
    private String nickname;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "phone", title = "手机号")
    private String phone;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "avatar", title = "头像")
    private String avatar;

    @Schema(name = "sex", title = "0:女 1:男")
    private Integer sex;

    @Schema(name = "summary", title = "个人描述")
    private String summary;

    @Schema(name = "ipAddress", title = "最后登录IP")
    private String ipAddress;

    @Schema(name = "ipRegion", title = "最后登录ip归属地")
    private String ipRegion;

    @Schema(name = "status", title = "1:禁用 0:正常")
    private Integer status;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否删除")
    private Integer isDeleted;

}