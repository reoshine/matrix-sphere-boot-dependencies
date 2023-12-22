package com.roshine.adp.base.client.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-13 21:13
 * @Description 访问用户对象
 */
@Data
@Accessors(chain = true)
public class LoginAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = -6755650601781710954L;

    @Schema(defaultValue = "主键")
    private Long id;

    @Schema(defaultValue = "账号")
    private String accountNo;

    @Schema(defaultValue = "账户名称")
    private String accountName;

    @Schema(defaultValue = "账户密码")
    private String accountPassword;

    @Schema(defaultValue = "电话号码")
    private String cellPhone;

    @Schema(defaultValue = "身份证号")
    private String idCardNo;

    @Schema(defaultValue = "账号状态")
    private Integer accountStatus;

}
