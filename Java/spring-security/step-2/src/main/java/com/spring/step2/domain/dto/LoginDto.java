package com.spring.step2.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", title = "LoginRequest登录参数", description = "登录请求参数")
public class LoginDto {

    @Schema(name = "type", description = "登录类型")
    private String type = "default";

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", description = "密码")
    private String password;

}
