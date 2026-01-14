package com.spring.step2.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "username", title = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "email", title = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

}