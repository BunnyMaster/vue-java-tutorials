package com.spring.step2.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "UserDTO对象", title = "用户", description = "用户的DTO对象")
public class UserDto {

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "email", title = "邮箱")
    private String email;

}