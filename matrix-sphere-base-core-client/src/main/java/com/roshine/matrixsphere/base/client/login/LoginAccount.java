package com.roshine.matrixsphere.base.client.login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author roshine
 * @version 2.0.0
 * 全局微服务访问用户上下文 (与 SSO Account 领域模型对齐)
 */
@Data
@Accessors(chain = true)
public class LoginAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = -6755650601781710954L;

    /**
     * 用户ID (全局唯一标识)
     */
    private String id;

    /**
     * 登录账号 (与 SSO 的 accountNo 对齐)
     */
    private String accountNo;

    /**
     * 账户名称/真实姓名
     */
    private String accountName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 电话号码 (与 SSO 对齐)
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 该账号拥有的角色或权限标识集合 (RBAC)
     */
    private Set<String> authorities;
}