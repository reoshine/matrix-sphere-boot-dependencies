package com.roshine.adp.base.core.handler;

import com.roshine.adp.base.client.exception.AssertException;
import com.roshine.adp.base.client.exception.BaseException;
import com.roshine.adp.base.client.response.Response;
import com.roshine.adp.base.client.response.ResultCode;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author luoxin
 * @version 1.0.0
 * @date 2023-04-15 21:48
 * @description
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
        return Response.<ResultCode>init().failed(ResultCode.ERROR, e.getMessage());
    }

    @ExceptionHandler({BaseException.class})
    public Response<ResultCode> illegalStateExceptionHandler(BaseException e) {
        return Response.<ResultCode>init().failed(ResultCode.ERROR, e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public Response<ResultCode> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return Response.<ResultCode>init().failed(ResultCode.ERROR, e.getMessage());
    }

    @ExceptionHandler({AssertException.class})
    public Response<ResultCode> assertExceptionHandler(AssertException e) {
        return Response.<ResultCode>init().failed(ResultCode.ERROR, e.getMessage());
    }
}
