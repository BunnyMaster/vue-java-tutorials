package com.spring.official.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EmailUsersVO对象", title = "邮箱发送表", description = "邮箱发送表的VO对象")
public class EmailUsersVo {

    @Schema(name = "id", title = "主键")
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

