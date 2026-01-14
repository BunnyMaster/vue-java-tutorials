package cn.bunny.pojo.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {
    // 成功操作 200
    SUCCESS(200, "操作成功"),
    SUCCESS_LOGOUT(200, "退出成功"),
    EMAIL_CODE_REFRESH(200, "邮箱验证码已刷新"),
    // 验证错误 201
    USERNAME_NOT_EMPTY(201, "用户名不能为空"),
    PASSWORD_NOT_EMPTY(201, "密码不能为空"),
    EMAIL_CODE_NOT_EMPTY(201, "邮箱验证码不能为空"),
    SEND_EMAIL_CODE_NOT_EMPTY(201, "请先发送邮箱验证码"),
    EMAIL_CODE_NOT_MATCHING(201, "邮箱验证码不匹配"),
    LOGIN_ERROR(201, "账号或密码错误"),
    LOGIN_ERROR_USERNAME_PASSWORD_NOT_EMPTY(201, "登录信息不能为空"),
    // 数据相关 206
    ILLEGAL_REQUEST(206, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    DATA_ERROR(206, "数据异常"),
    // 身份过期 208
    LOGIN_AUTH(208, "请先登陆"),
    AUTHENTICATION_EXPIRED(208, "身份验证过期"),
    SESSION_EXPIRATION(208, "会话过期"),
    // 封禁 209
    FAIL_NO_ACCESS_DENIED_USER_LOCKED(209, "该账户被封禁"),
    THE_SAME_USER_HAS_LOGGED_IN(209, "相同用户已登录"),
    // 提示错误
    URL_ENCODE_ERROR(216, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR(217, "非法回调请求"),
    FETCH_USERINFO_ERROR(219, "获取用户信息失败"),
    // 无权访问 403
    FAIL_REQUEST_NOT_AUTH(403, "用户未认证"),
    FAIL_NO_ACCESS_DENIED(403, "无权访问"),
    FAIL_NO_ACCESS_DENIED_USER_OFFLINE(403, "用户强制下线"),
    LOGGED_IN_FROM_ANOTHER_DEVICE(403, "没有权限访问"),
    // 系统错误 500
    SERVICE_ERROR(500, "服务异常"),
    FAIL(500, "失败"),
    ;

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}