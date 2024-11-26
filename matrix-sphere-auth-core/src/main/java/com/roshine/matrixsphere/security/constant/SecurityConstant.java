package com.roshine.matrixsphere.security.constant;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2022-06-19 10:40
 * @description
 */
public class SecurityConstant {

    /**
     * redisToken前缀
     */
    public final static String CACHE_TOKEN_PREFIX = "ADP-SSO:";

    /**
     * registered_client前缀
     */
    public final static String REGISTERED_CLIENT = "REGISTERED-CLIENT:";

    /**
     * 登录页面
     */
    public final static String LOGIN_PAGE = "login";

    /**
     * 去登录页地址
     */
    public final static String LOGIN_URI = "/sso/login";

    /**
     * 去登录页地址
     */
    public final static String CLIENT_LOGIN_URI = "/client/login";

    /**
     * 认证成功回调地址
     */
    public static final String LOGIN_CALLBACK_URI = "/sso/callback";

    /**
     * 表单登录请求
     */
    public final static String LOGIN_PROCESSING_URI = "/authentication/login";

    /**
     * 认证url
     */
    public static final String OAUTH_AUTHORIZE_URL = "/oauth2/authorize";

    /**
     * 获取token url
     */
    public final static String OAUTH_TOKEN_URL = "/oauth2/token";


    /**
     * oauth2内省uri
     */
    public final static String OAUTH_INTROSPECT = "/oauth2/introspect";

    /**
     * 登出请求
     */
    public final static String LOGOUT_URI = "/sso/logout";

    /**
     * clientId
     */
    public final static String CLIENT_ID = "adp-matrix";

    /**
     * clientSecret
     */
    public final static String CLIENT_SECRET = "adp-matrix-secret";

    /**
     * access_token
     */
    public final static String ACCESS_TOKEN = "access_token";

    /**
     * refresh_token
     */
    public final static String REFRESH_TOKEN = "refresh_token";
}
