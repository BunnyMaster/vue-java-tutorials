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
@TableName("sys_email_users")
@Schema(name = "EmailUsers对象", title = "邮箱发送表", description = "邮箱发送表的实体类对象")
public class EmailUsersEntity {

    @Schema(name = "id", title = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "host", title = "Host地址")
    private String host;

    @Schema(name = "port", title = "端口号")
    private Integer port;

    @Schema(name = "smtpAgreement", title = "邮箱协议")
    private String smtpAgreement;

    @Schema(name = "openSsl", title = "是否启用ssl")
    private Integer openSsl;

    @Schema(name = "isDefault", title = "是否为默认邮件")
    private Integer isDefault;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "更新用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否被删除")
    private Boolean isDeleted;

}