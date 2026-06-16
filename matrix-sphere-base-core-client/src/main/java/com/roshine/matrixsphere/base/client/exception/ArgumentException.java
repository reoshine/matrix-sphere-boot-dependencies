package com.roshine.matrixsphere.base.client.exception;

import java.io.Serial;

/**
 * <p>参数异常</p>
 * <p>在处理业务过程中校验参数出现错误, 可以抛出该异常</p>
 *
 * @author luoxin
 * @version 2.0.0
 */
public class ArgumentException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4416491166499405436L;

    public ArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ArgumentException(String message) {
        super(400, message); // 默认 400 Bad Request
    }

    public ArgumentException(Exception e) {
        super(400, e.getMessage() != null ? e.getMessage() : "参数校验异常");
        this.initCause(e); // 传递原生异常堆栈，方便排查
    }

    public ArgumentException(ErrorCode errorCode, String extendMessage) {
        // 将 ErrorCode 原本的 message 和 扩展 message 拼接起来
        super(errorCode.getCode(), errorCode.getMessage() + ": " + extendMessage);
    }
}