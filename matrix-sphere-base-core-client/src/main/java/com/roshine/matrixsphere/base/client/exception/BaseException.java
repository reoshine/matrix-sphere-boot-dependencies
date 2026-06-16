package com.roshine.matrixsphere.base.client.exception;

import lombok.Getter;

/**
 * @author roshine
 * @version 2.0.0
 * 全局基础异常类 (统一继承 RuntimeException)
 */
@Getter
public class BaseException extends RuntimeException {

    private final Integer code;

    public BaseException(String message) {
        super(message);
        this.code = 500; // 默认系统内部错误
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}