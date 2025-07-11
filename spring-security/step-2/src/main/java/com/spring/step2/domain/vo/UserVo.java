package com.spring.step2.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserVO对象", title = "用户", description = "用户的VO对象")
public class UserVo {

    @Schema(name = "id", title = "主键")
    private String id;

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "email", title = "邮箱")
    private String email;

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

