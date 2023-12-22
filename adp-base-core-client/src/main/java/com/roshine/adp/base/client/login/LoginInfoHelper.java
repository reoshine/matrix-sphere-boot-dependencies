package com.roshine.adp.base.client.login;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-13 19:01
 * @Description 登录信息
 */
public class LoginInfoHelper {

    private static ThreadLocal<LoginInfo> local = new ThreadLocal<>();

    public static void setUserInfo(LoginInfo loginInfo) {
        local.set(loginInfo);
    }

    public static LoginInfo getUserInfo() {
        return local.get();
    }

    public static void removeUserInfo() {
        local.remove();
    }
}
