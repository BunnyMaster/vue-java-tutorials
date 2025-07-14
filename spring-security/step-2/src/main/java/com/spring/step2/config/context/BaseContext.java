package com.spring.step2.config.context;


public class BaseContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();

    // 用户id相关
    public static Long getUserId() {
        return userId.get();
    }

    public static void setUserId(Long _userId) {
        userId.set(_userId);
    }

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String _username) {
        username.set(_username);
    }

    public static void removeUser() {
        username.remove();
        userId.remove();
    }
}