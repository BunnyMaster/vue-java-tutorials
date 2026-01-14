package com.redis.redis.exception;

import com.redis.redis.model.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GatewayBusinessExceptionHandler {

    // 请求方法不支持错误
    private static final Pattern METHOD_NOT_SUPPORTED_PATTERN = Pattern.compile("Request method '(.*?)' is not supported");

    /**
     * 参数验证异常 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Object>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数校验失败: {}", errorMsg, exception);

        // 属性转换失败 - 合并到通用异常处理器中处理
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, errorMsg));
    }

    /**
     * 通用异常处理器 - 放在最后
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Result<Object>> handleGeneralException(Exception exception) {
        String message = exception.getMessage();

        log.error("服务器异常: {}", message, exception);

        // 设置默认错误消息值
        message = StringUtils.hasText(message) ? message : "服务器内部错误";

        // 方法不支持错误
        Matcher methodErrorMatcher = METHOD_NOT_SUPPORTED_PATTERN.matcher(message);
        if (methodErrorMatcher.find()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(Result.error(405, "不支持请求方法" + methodErrorMatcher.group(1)));
        }

        // 默认服务器错误
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(null, 500, message));
    }

}