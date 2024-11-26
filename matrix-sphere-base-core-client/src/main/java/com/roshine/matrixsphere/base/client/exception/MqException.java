package com.roshine.matrixsphere.base.client.exception;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2021-01-24 1:39
 * @description mq异常类
 */
public class MqException extends RuntimeException {

    private static final long serialVersionUID = -2331080385236476708L;

    public MqException() {}

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }
}
