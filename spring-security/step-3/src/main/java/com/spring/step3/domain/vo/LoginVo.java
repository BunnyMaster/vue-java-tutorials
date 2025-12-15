package com.spring.step3.domain.vo;


import com.spring.step3.domain.vo.base.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回内容
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginVo对象", title = "登录成功返回内容", description = "登录成功返回内容")
public class LoginVo extends BaseVo {

    @Schema(name = "nickname", title = "昵称")
    private String nickname;

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "token", title = "令牌")
    private String token;

    @Schema(name = "expires", title = "过期时间")
    private String expires;

    @Schema(name = "readMeDay", title = "记住我多久")
    private Long readMeDay;

}