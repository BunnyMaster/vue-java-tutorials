package com.spring.step2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("users")
@Schema(name = "Users对象", title = "", description = "的实体类对象")
public class UsersEntity {

    @Schema(name = "username", title = "username")
    @TableId(type = IdType.ASSIGN_ID)
    private String username;

    @Schema(name = "password", title = "password")
    private String password;

    @Schema(name = "id", title = "主键")
    private String id;

    @Schema(name = "email", title = "邮箱")
    private String email;

}