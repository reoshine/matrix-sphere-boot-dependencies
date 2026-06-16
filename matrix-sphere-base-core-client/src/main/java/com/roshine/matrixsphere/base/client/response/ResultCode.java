package com.roshine.matrixsphere.base.client.response;

import com.roshine.matrixsphere.base.client.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author roshine
 * @version 2.0.0
 * 全局通用响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements ErrorCode {

    SUCCESS(200, "操作成功"),
    FAILURE(500, "系统内部异常"),
    UNAUTHORIZED(401, "未登录或会话已过期"),
    FORBIDDEN(403, "权限不足，拒绝访问"),
    VALIDATE_FAILED(400, "参数检验失败");

    private final Integer code;
    private final String message; // 统一叫 message，而不是 msg
}