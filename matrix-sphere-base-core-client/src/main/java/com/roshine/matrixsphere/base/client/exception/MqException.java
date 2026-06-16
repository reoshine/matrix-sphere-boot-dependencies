package com.roshine.matrixsphere.base.client.exception;

/**
 * @author roshine
 * @version 2.0.0
 * @description mq异常类
 */
public class MqException extends BaseException {

    public MqException(String message) {
        super(message);
    }

    public MqException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MqException(ErrorCode errorCode, String extendMessage) {
        super(errorCode.getCode(), errorCode.getMessage() + ": " + extendMessage);
    }
}