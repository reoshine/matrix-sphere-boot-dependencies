package com.roshine.matrixsphere.base.core.handler;

import com.roshine.matrixsphere.base.client.exception.BaseException;
import com.roshine.matrixsphere.base.client.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author roshine
 * @version 2.0.0
 * 全局统一异常拦截器 (Core Layer)
 * 核心职责：拦截所有微服务抛出的异常，统一转换为标准的 Response JSON。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 拦截参数校验异常 (@Valid / @Validated)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("参数校验失败: ");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                    .append(" ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        String msg = errorMessage.toString();
        log.warn("接口参数校验失败: {}", msg);
        return Response.fail(400, msg);
    }

    /**
     * 2. 拦截自定义业务异常 (统一定义的 BaseException 及其子类)
     */
    @ExceptionHandler(BaseException.class)
    public Response<Void> handleBaseException(BaseException e) {
        log.warn("业务异常 [{}]: {}", e.getCode(), e.getMessage());
        return Response.fail(e.getCode(), e.getMessage());
    }

    /**
     * 3. 拦截非法参数断言异常 (如 Assert.notNull 或 Domain 层抛出的 IllegalArgumentException)
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public Response<Void> handleIllegalException(RuntimeException e) {
        log.warn("非法状态/参数拦截: {}", e.getMessage());
        return Response.fail(400, e.getMessage());
    }

    /**
     * 4. 兜底拦截所有未知的系统级异常 (500)
     */
    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        // 未知异常必须打印完整堆栈，方便排查
        log.error("系统发生未捕获的严重内部异常: ", e);
        return Response.fail(500, "系统内部繁忙，请稍后再试或联系管理员");
    }
}