package com.roshine.matrixsphere.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2022-06-16 22:48
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = -6155520593458223103L;

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "账号")
    private String accountNo;

    @Schema(description = "账号状态")
    private Integer accountStatus;

    @Schema(description = "账户名称")
    private String accountName;

    @Schema(description = "账户密码")
    private String accountPassword;

    @Schema(description = "电话号码")
    private String cellPhone;

    @Schema(description = "身份证号")
    private String idCardNo;

    private Collection<? extends GrantedAuthority> authorities;
    /**
     * 帐号是否过期
     */
    private boolean accountNonExpired = true;
    /**
     * 认证是否过期
     */
    private boolean credentialsNonExpired = true;
    /**
     * 帐号是否锁定
     */
    private boolean accountNonLocked = true;
    /**
     * 帐号是否删除
     */
    private boolean enabled = true;

    @Override
    public String getPassword() {
        return accountPassword;
    }

    @Override
    public String getUsername() {
        return accountName;
    }
}
