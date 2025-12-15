package cn.bunny.pojo.result.constant;

import lombok.Data;


@Data
public class ExceptionConstant {
    public static final String UNKNOWN_Exception = "未知错误";
    public static final String TOKEN_IS_EMPTY = "token为空";
    public static final String DATA_IS_EMPTY = "数据为空";
    public static final String REQUEST_DATA_NOT_EMPTY_Exception = "请求参数为空";
    public static final String UPDATE_DTO_IS_NULL_Exception = "修改参数为空";
    public static final String ADD_DATA_IS_EMPTY_Exception = "添加数据为空";
    public static final String DELETE_ID_IS_NOT_EMPTY_Exception = "删除id不能为空";
    // 文章操作相关
    public static final String DO_LIKE_COMMENT_NOT_EXIST = "点赞内容不存在";
    public static final String REPLY_USER_EMPTY_EXCEPTION = "回复的用户不存在";
    public static final String REPLY_USER_ID_EMPTY_EXCEPTION = "回复的用户不能为空";
    public static final String MENU_IS_NOT_EXIST_Exception = "菜单不存在";
    public static final String POST_COMMENT_EMPTY_Exception = "评论内容不能为空";
    public static final String ARTICLE_ID_NOT_EMPTY_Exception = "文章id不能为空";
    public static final String UPDATE_ID_IS_NOT_EMPTY_Exception = "修改id不能为空";
    public static final String CANNOT_TOP_OTHER_USER = "不能操作此内容";
    public static final String ARTICLE_NOT_FOUND_EXCEPTION = "文章未找到";
    // 登录相关
    public static final String USER_TOKEN_OUT_OF_DATE_Exception = "用户登录过期";
    public static final String LOGIN_DTO_IS_EMPTY_Exception = "登录参数不能为空";
    public static final String LOGIN_FAILED_Exception = "登录失败";
    // 账号相关
    public static final String ACCOUNT_NOT_FOUND_Exception = "账号不存在";
    public static final String ACCOUNT_LOCKED_Exception = "账号被锁定";
    // 用户相关
    public static final String USER_NOT_LOGIN_Exception = "用户未登录";
    public static final String USERNAME_IS_EMPTY_Exception = "用户名不能为空";
    public static final String ALREADY_USER_Exception = "用户已存在";
    public static final String USER_NOT_FOUND_Exception = "用户不存在";
    // 密码相关
    public static final String PASSWORD_Exception = "密码错误";
    public static final String PASSWORD_NOT_EMPTY_Exception = "密码不能为空";
    public static final String OLD_PASSWORD_Exception = "旧密码不匹配";
    public static final String PASSWORD_EDIT_Exception = "密码修改失败";
    public static final String OLD_PASSWORD_SAME_NEW_PASSWORD_Exception = "旧密码与新密码相同";
    // 验证码错误
    public static final String PLEASE_SEND_EMAIL_CODE_Exception = "请先发送验证码";
    public static final String MESSAGE_CODE_NOT_PASS_Exception = "短信验证码未过期";
    public static final String MESSAGE_CODE_UNAUTHORIZED_Exception = "短信验证码未授权，请联系管理员";
    public static final String VERIFICATION_CODE_ERROR_Exception = "验证码错误";
    public static final String CAPTCHA_IS_EMPTY_Exception = "验证码不能为空";
    public static final String KEY_IS_EMPTY_Exception = "验证码key不能为空";
    public static final String VERIFICATION_CODE_DOES_NOT_MATCH_Exception = "验证码不匹配";
    public static final String VERIFICATION_CODE_IS_EMPTY_Exception = "验证码失效或不存在";
}
