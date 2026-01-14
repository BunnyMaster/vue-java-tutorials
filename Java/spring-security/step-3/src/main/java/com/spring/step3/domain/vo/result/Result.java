package com.spring.step3.domain.vo.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    // 状态码
    private Integer code;
    // 返回消息
    private String message;
    // 返回数据
    private T data;
    // 权限范围
    private List<String> auths;

    /**
     * 自定义返回体
     *
     * @param data 返回体
     * @return Result<T>
     */
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    /**
     * 自定义返回体
     *
     * @param data  返回体
     * @param auths 权限范围
     * @return Result<T>
     */
    protected static <T> Result<T> build(T data, List<String> auths) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setAuths(auths);
        return result;
    }

    /**
     * 自定义返回体，使用ResultCodeEnum构建
     *
     * @param data     返回体
     * @param codeEnum 返回状态码
     * @return {@link Result<T>}
     */
    public static <T> Result<T> build(T data, ResultCodeEnum codeEnum) {
        Result<T> result = build(data);
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getMessage());
        return result;
    }

    /**
     * 自定义返回体，使用ResultCodeEnum构建
     *
     * @param data     返回体
     * @param codeEnum 返回状态码
     * @return {@link Result<T>}
     */
    public static <T> Result<T> build(T data, List<String> auths, ResultCodeEnum codeEnum) {
        Result<T> result = build(data);
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getMessage());
        result.setAuths(auths);
        return result;
    }

    /**
     * 自定义返回体，使用ResultCodeEnum构建
     *
     * @param codeEnum 返回状态码
     * @return Result<T>
     */
    public static <T> Result<T> build(List<String> auths, ResultCodeEnum codeEnum) {
        Result<T> result = build(null);
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getMessage());
        result.setAuths(auths);
        return result;
    }

    /**
     * 自定义返回体
     *
     * @param body    返回体
     * @param code    返回状态码
     * @param message 返回消息
     * @param auths   权限范围
     * @return {@link Result<T>}
     */
    public static <T> Result<T> build(T body, Integer code, String message, List<String> auths) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        result.setAuths(auths);
        return result;
    }

    /**
     * 操作成功
     *
     * @return Result<T>
     */
    public static <T> Result<T> success() {
        return success(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 操作成功
     *
     * @param data baseCategory1List
     */
    public static <T> Result<T> success(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 操作成功
     *
     * @param data  baseCategory1List
     * @param auths 权限范围
     */
    public static <T> Result<T> success(T data, List<String> auths) {
        return build(data, auths, ResultCodeEnum.SUCCESS);
    }

    /**
     * 操作成功-状态码
     *
     * @param codeEnum 状态码
     */
    public static <T> Result<T> success(ResultCodeEnum codeEnum) {
        return success(null, codeEnum);
    }

    /**
     * 操作成功-状态码
     *
     * @param codeEnum 状态码
     */
    public static <T> Result<T> success(T data, ResultCodeEnum codeEnum) {
        return success(data, new ArrayList<>(), codeEnum);
    }

    /**
     * 操作成功-状态码
     *
     * @param codeEnum 状态码
     */
    public static <T> Result<T> success(List<String> auths, ResultCodeEnum codeEnum) {
        return build(null, auths, codeEnum);
    }

    /**
     * 操作成功-自定义返回数据和状态码
     *
     * @param data     返回体
     * @param auths    权限范围
     * @param codeEnum 状态码
     */
    public static <T> Result<T> success(T data, List<String> auths, ResultCodeEnum codeEnum) {
        return build(data, auths, codeEnum);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param message 错误信息
     */
    public static <T> Result<T> success(T data, String message) {
        return build(data, 200, message, new ArrayList<>());
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> success(T data, Integer code, String message) {
        return build(data, code, message, new ArrayList<>());
    }

    /**
     * 操作失败
     */
    public static <T> Result<T> error() {
        return Result.build(null);
    }

    /**
     * 操作失败-自定义返回数据
     *
     * @param data 返回体
     */
    public static <T> Result<T> error(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    /**
     * 操作失败-状态码
     *
     * @param codeEnum 状态码
     */
    public static <T> Result<T> error(ResultCodeEnum codeEnum) {
        return build(null, codeEnum);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data     返回体
     * @param codeEnum 状态码
     */
    public static <T> Result<T> error(T data, ResultCodeEnum codeEnum) {
        return build(data, codeEnum);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> error(T data, Integer code, String message) {
        return build(data, code, message, new ArrayList<>());
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param message 错误信息
     */
    public static <T> Result<T> error(T data, String message) {
        return build(null, 500, message, new ArrayList<>());
    }
}
