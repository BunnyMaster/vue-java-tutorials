package com.auth.ai.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Result通用返回结果", title = "通用返回结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    @Schema(name = "code", title = "状态码")
    private Integer code;

    @Schema(name = "messages", title = "返回消息")
    private String message;

    @Schema(name = "data", title = "返回数据")
    private T data;

    @Schema(name = "timestamp", title = "系统时间戳")
    private long timestamp = System.currentTimeMillis();

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
     * @param body    返回体
     * @param code    返回状态码
     * @param message 返回消息
     * @return {@link Result<T>}
     */
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        result.setData(body);
        return result;
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data 返回体
     */
    public static <T> Result<T> success(T data) {
        return build(data, 200, "操作成功");
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param message 错误信息
     */
    public static <T> Result<T> success(T data, String message) {
        return build(data, 200, message);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> success(T data, Integer code, String message) {
        return build(data, code, message);
    }

    /**
     * 操作失败
     */
    public static <T> Result<T> error() {
        return Result.build(null);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> error(T data, Integer code, String message) {
        return build(data, code, message);
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data 返回体
     */
    public static <T> Result<T> error(T data) {
        return build(data, 500, "操作失败");
    }

    /**
     * 操作失败-自定义返回数据和状态码
     *
     * @param data    返回体
     * @param message 错误信息
     */
    public static <T> Result<T> error(T data, String message) {
        return build(data, 500, message);
    }

}
