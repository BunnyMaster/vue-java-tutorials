package cn.bunny.common.service.context;


public class BaseContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<Long> adminId = new ThreadLocal<>();
    private static final ThreadLocal<String> adminName = new ThreadLocal<>();

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

    // adminId 相关
    public static Long getAdminId() {
        return adminId.get();
    }

    public static void setAdminId(Long _adminId) {
        adminId.set(_adminId);
    }

    public static String getAdminName() {
        return adminName.get();
    }

    public static void setAdminName(String _adminName) {
        adminName.set(_adminName);
    }

    public static void removeAdmin() {
        adminName.remove();
        adminId.remove();
    }
}