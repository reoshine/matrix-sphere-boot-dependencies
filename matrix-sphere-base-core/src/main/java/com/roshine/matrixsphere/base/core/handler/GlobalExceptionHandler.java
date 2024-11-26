package com.roshine.matrixsphere.base.core.handler;

import cn.hutool.core.util.StrUtil;
import com.roshine.matrixsphere.base.client.exception.AssertException;
import com.roshine.matrixsphere.base.client.exception.BaseException;
import com.roshine.matrixsphere.base.client.response.Response;
import com.roshine.matrixsphere.base.client.response.ResultCode;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author luoxin
 * @version 1.0.0
 * @date 2023-04-15 21:48
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response<ResultCode> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return Response.<ResultCode>init().failed(ResultCode.PARAM_VALIDATOR_ERROR, objectError.getDefaultMessage());
    }

    @ExceptionHandler({IllegalStateException.class})
    public Response<ResultCode> illegalStateExceptionHandler(IllegalStateException e) {
        String message = e.getMessage();
        if (StrUtil.isNotBlank(message)) {
            message = message.substring(message.indexOf(":") + 1);
        }
        return Response.<ResultCode>init().failed(ResultCode.ERROR, message);
    }

    @ExceptionHandler({BaseException.class})
    public Response<ResultCode> illegalStateExceptionHandler(BaseException e) {
        String message = e.getMessage();
        if (StrUtil.isNotBlank(message)) {
            message = message.substring(message.indexOf(":") + 1);
        }
        return Response.<ResultCode>init().failed(ResultCode.ERROR, message);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public Response<ResultCode> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        String message = e.getMessage();
        if (StrUtil.isNotBlank(message)) {
            message = message.substring(message.indexOf(":") + 1);
        }
        return Response.<ResultCode>init().failed(ResultCode.ERROR, message);
    }

    @ExceptionHandler({AssertException.class})
    public Response<ResultCode> assertExceptionHandler(AssertException e) {
        String message = e.getMessage();
        if (StrUtil.isNotBlank(message)) {
            message = message.substring(message.indexOf(":") + 1);
        }
        return Response.<ResultCode>init().failed(ResultCode.ERROR, message);
    }
}
