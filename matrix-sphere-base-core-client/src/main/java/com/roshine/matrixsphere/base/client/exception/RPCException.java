package com.roshine.matrixsphere.base.client.exception;

/**
 * @author roshine
 * @version 2.0.0
 * @Description 远程调用异常类
 */
public class RPCException extends BaseException {

    public RPCException(String message) {
        super(message);
    }

    public RPCException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RPCException(String message, Throwable cause) {
        super(500, message);
        this.initCause(cause);
    }
}