package com.roshine.matrixsphere.base.client.login;

/**
 * @author roshine
 * @version 2.0.0
 * 当前线程登录信息上下文句柄
 * 警告：在跨线程、异步任务中直接使用会导致 NullPointerException，必须配合上下文透传工具使用！
 */
public class LoginInfoHelper {

    private static final ThreadLocal<LoginInfo> LOCAL = new ThreadLocal<>();

    public static void setUserInfo(LoginInfo loginInfo) {
        LOCAL.set(loginInfo);
    }

    public static LoginInfo getUserInfo() {
        return LOCAL.get();
    }

    /**
     * 快捷获取当前登录用户 ID
     */
    public static String getUserId() {
        LoginInfo info = getUserInfo();
        return (info != null && info.getAccount() != null) ? info.getAccount().getId() : null;
    }

    public static void removeUserInfo() {
        LOCAL.remove();
    }
}