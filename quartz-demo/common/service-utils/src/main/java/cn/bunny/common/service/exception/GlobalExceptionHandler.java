package cn.bunny.common.service.exception;


import cn.bunny.pojo.result.Result;
import cn.bunny.pojo.result.ResultCodeEnum;
import cn.bunny.pojo.result.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 自定义异常信息
    @ExceptionHandler(BunnyException.class)
    @ResponseBody
    public Result<Object> exceptionHandler(BunnyException exception) {
        log.error("GlobalExceptionHandler===>自定义异常信息：{}", exception.getMessage());

        Integer code = exception.getCode() != null ? exception.getCode() : 500;
        return Result.error(null, code, exception.getMessage());
    }

    // 运行时异常信息
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> exceptionHandler(RuntimeException exception) throws FileNotFoundException {
        log.error("GlobalExceptionHandler===>运行时异常信息：{}", exception.getMessage());
        exception.printStackTrace();
        return Result.error(null, 500, "出错了啦");
    }

    // 捕获系统异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> error(Exception exception) {
        log.error("GlobalExceptionHandler===>系统异常信息：{}", exception.getMessage());

        return Result.error(null, 500, "系统异常");
    }

    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result<Object> error(ArithmeticException exception) {
        log.error("GlobalExceptionHandler===>特定异常信息：{}", exception.getMessage());

        return Result.error(null, 500, exception.getMessage());
    }

    // spring security异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result<String> error(AccessDeniedException exception) throws AccessDeniedException {
        log.error("GlobalExceptionHandler===>spring security异常：{}", exception.getMessage());

        return Result.error(ResultCodeEnum.SERVICE_ERROR);
    }

    // 处理SQL异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error("GlobalExceptionHandler===>处理SQL异常:{}", exception.getMessage());

        String message = exception.getMessage();
        if (message.contains("Duplicate entry")) {
            // 截取用户名
            String username = message.split(" ")[2];
            // 错误信息
            String errorMessage = username + ExceptionConstant.ALREADY_USER_Exception;
            return Result.error(errorMessage);
        } else {
            return Result.error(ExceptionConstant.UNKNOWN_Exception);
        }
    }
}
