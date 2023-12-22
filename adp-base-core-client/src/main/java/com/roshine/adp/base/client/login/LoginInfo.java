package com.roshine.adp.base.client.login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-13 19:15
 * @Description
 */
@Data
@Accessors(chain = true)
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = 5316505219903504682L;

    /**
     * 用户信息
     */
    private LoginAccount account;
}
