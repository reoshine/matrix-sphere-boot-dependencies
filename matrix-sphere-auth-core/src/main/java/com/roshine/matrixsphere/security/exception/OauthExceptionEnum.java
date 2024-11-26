package com.roshine.matrixsphere.security.exception;

import com.roshine.matrixsphere.base.client.exception.ErrorCode;

/**
 * @author luoxin
 * @version 1.0.0
 * @date 2023-09-04 21:32
 */
public enum OauthExceptionEnum implements ErrorCode {

    CLIENT_ID_NOT_EXIST(41001, "客户端id不存在"),
    ;

    private final Integer code;

    private final String desc;

    OauthExceptionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getErrorMessage() {
        return this.desc;
    }
}
