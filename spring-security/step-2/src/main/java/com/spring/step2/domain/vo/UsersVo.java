package com.spring.step2.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UsersVO对象", title = "", description = "的VO对象")
public class UsersVo {

    @Schema(name = "username", title = "username")
    private String username;

    @Schema(name = "password", title = "password")
    private String password;

    @Schema(name = "enabled", title = "enabled")
    private Boolean enabled;

    @Schema(name = "id", title = "主键")
    private String id;

    @Schema(name = "email", title = "邮箱")
    private String email;

}

