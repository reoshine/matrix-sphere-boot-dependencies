package com.roshine.matrixsphere.base.client.exception;

import java.io.Serial;

/**
 * <p>参数异常</p>
 * <p>在处理业务过程中校验参数出现错误, 可以抛出该异常</p>
 * <p>编写公共代码（如工具类）时，对传入参数检查不通过时，可以抛出该异常</p>
 *
 * @author luoxin
 * @version 1.0.0
 * @date 2023-04-19 22:28
 */
public class ArgumentException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4416491166499405436L;

    public ArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ArgumentException(ErrorCode errorCode, String extendMessage) {
        super(errorCode, extendMessage);
    }
}
