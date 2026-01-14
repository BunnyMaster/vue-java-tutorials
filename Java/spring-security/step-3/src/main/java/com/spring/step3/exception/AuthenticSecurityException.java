package com.spring.step3.exception;

import com.spring.step3.domain.vo.result.ResultCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@ToString
@Slf4j
public class AuthenticSecurityException extends RuntimeException {
    // 状态码
    Integer code;

    // 描述信息
    String message = "服务异常";

    // 返回结果状态
    ResultCodeEnum resultCodeEnum;

    public AuthenticSecurityException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AuthenticSecurityException(String message) {
        super(message);
        this.message = message;
    }

    public AuthenticSecurityException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.resultCodeEnum = codeEnum;
    }

    public AuthenticSecurityException(String message, Exception exception) {
        super(message);
        this.message = message;
        log.error(message, exception);
    }
}
