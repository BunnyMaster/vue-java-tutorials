package com.spring.step3.exception;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 全局异常拦截器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 自定义异常信息
    @ExceptionHandler(AuthenticSecurityException.class)
    @ResponseBody
    public Result<Object> exceptionHandler(AuthenticSecurityException exception) {
        Integer code = exception.getCode() != null ? exception.getCode() : 500;
        return Result.error(null, code, exception.getMessage());
    }

    // 运行时异常信息
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> exceptionHandler(RuntimeException exception) {
        String message = exception.getMessage();
        message = StringUtils.hasText(message) ? message : "服务器异常";
        log.error("发生业务异常: {}", exception.getMessage(), exception);

        // 💡IDEA：如果需要特殊情况的日志可以参考下面的代码
        // =========================================
        // StringWriter sw = new StringWriter();
        // e.printStackTrace(new PrintWriter(sw));
        // logger.error(sw.toString());
        // =========================================

        // 解析异常
        String jsonParseError = "JSON parse error (.*)";
        Matcher jsonParseErrorMatcher = Pattern.compile(jsonParseError).matcher(message);
        if (jsonParseErrorMatcher.find()) {
            return Result.error(null, 500, "JSON解析异常 " + jsonParseErrorMatcher.group(1));
        }

        // 数据过大
        String dataTooLongError = "Data too long for column (.*?) at row 1";
        Matcher dataTooLongErrorMatcher = Pattern.compile(dataTooLongError).matcher(message);
        if (dataTooLongErrorMatcher.find()) {
            return Result.error(null, 500, dataTooLongErrorMatcher.group(1) + " 字段数据过大");
        }

        // 主键冲突
        String primaryKeyError = "Duplicate entry '(.*?)' for key .*";
        Matcher primaryKeyErrorMatcher = Pattern.compile(primaryKeyError).matcher(message);
        if (primaryKeyErrorMatcher.find()) {
            return Result.error(null, 500, "[" + primaryKeyErrorMatcher.group(1) + "]已存在");
        }

        // corn表达式错误
        String cronExpression = "CronExpression '(.*?)' is invalid";
        Matcher cronExpressionMatcher = Pattern.compile(cronExpression).matcher(message);
        if (cronExpressionMatcher.find()) {
            return Result.error(null, 500, "表达式 " + cronExpressionMatcher.group(1) + " 不合法");
        }

        log.error("GlobalExceptionHandler===>运行时异常信息：{}", message);
        return Result.error(null, 500, message);
    }

    // 表单验证字段
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(", "));
        return Result.error(null, 201, errorMessage);
    }

    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result<Object> error(ArithmeticException exception) {
        log.error("GlobalExceptionHandler===>特定异常信息：{}", exception.getMessage());

        return Result.error(null, 500, exception.getMessage());
    }

    // 处理SQL异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error("GlobalExceptionHandler===>处理SQL异常:{}", exception.getMessage());

        String message = exception.getMessage();
        if (message.contains("Duplicate entry")) {
            // 错误信息
            return Result.error(ResultCodeEnum.USER_IS_EMPTY);
        } else {
            return Result.error(ResultCodeEnum.UNKNOWN_EXCEPTION);
        }
    }

    // 处理无权访问异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> handleAccessDenied(AccessDeniedException exception) {
        return Result.error(exception.getMessage(), ResultCodeEnum.FAIL_NO_ACCESS_DENIED);
    }
}
