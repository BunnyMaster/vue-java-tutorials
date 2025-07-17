package com.spring.step3.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义未认证异常
 */
public class MyAuthenticationException extends AuthenticationException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public MyAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}