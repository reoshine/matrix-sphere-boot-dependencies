package com.roshine.matrixsphere.base.client.exception;

/**
 * @author roshine
 * @version 2.0.0
 * 统一错误码契约接口 (Client Layer)
 * 核心职责：强制所有的业务枚举类必须提供 code 和 message。
 */
public interface ErrorCode {

    /**
     * 获取错误状态码
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取错误提示信息
     * @return 提示信息
     */
    String getMessage();
}