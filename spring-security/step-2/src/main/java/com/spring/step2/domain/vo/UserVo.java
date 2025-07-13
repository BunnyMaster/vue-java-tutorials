package com.spring.step2.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserVO对象", title = "用户基本信息表", description = "用户基本信息表的VO对象")
public class UserVo {

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户ID")
    private Long createUser;

    @Schema(name = "updateUser", title = "更新用户ID")
    private Long updateUser;

}

