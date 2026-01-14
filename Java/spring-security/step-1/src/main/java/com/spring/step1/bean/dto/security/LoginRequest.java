package com.spring.step1.bean.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "LoginRequest", title = "LoginRequest登录参数", description = "登录请求参数")
public class LoginRequest {

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", description = "密码")
    private String password;

}
