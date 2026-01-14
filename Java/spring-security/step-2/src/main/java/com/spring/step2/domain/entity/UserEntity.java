package com.spring.step2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.step2.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
@Schema(name = "User对象", title = "用户", description = "用户的实体类对象")
public class UserEntity extends BaseEntity {

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "isDeleted", title = "是否被删除")
    @TableField(exist = false)
    private Boolean isDeleted;

}