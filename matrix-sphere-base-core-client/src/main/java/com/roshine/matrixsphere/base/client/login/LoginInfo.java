package com.roshine.matrixsphere.base.client.login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 2.0.0
 * 登录信息外层包装 (可扩展如 Token、ClientID 等上下文信息)
 */
@Data
@Accessors(chain = true)
public class LoginInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5316505219903504682L;

    /**
     * 核心用户信息
     */
    private LoginAccount account;

    /**
     * 当前请求使用的 Token 值 (可选扩展)
     */
    private String tokenValue;
}