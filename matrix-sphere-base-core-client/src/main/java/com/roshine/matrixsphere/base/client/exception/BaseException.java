package com.roshine.matrixsphere.base.client.exception;

import com.roshine.matrixsphere.base.client.utils.CommonUtils;

import java.io.Serial;

/**
 * <p>
 * 基础异常类，所有自定义异常类都需要继承本类
 * </p>
 *
 * @author luoxin
 * @version 1.0.0
 * @date 2023-04-19 22:24
 */
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6672301619102513944L;

    /**
     * 异常码
     */
    protected ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String extendMessage) {
        super(CommonUtils.format(errorCode.getErrorMessage(), extendMessage));
        this.errorCode = errorCode;
    }
}
