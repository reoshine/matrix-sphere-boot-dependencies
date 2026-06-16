package com.roshine.matrixsphere.base.client.exception;

/**
 * @author roshine
 * @version 2.0.0
 * @Description token过期异常
 */
public class TokenIsExpiredException extends BaseException {

    // 无参构造，直接抛出标准的 401 提示
    public TokenIsExpiredException() {
        super(401, "Token已过期或无效，请重新登录");
    }

    public TokenIsExpiredException(String message) {
        super(401, message);
    }
}