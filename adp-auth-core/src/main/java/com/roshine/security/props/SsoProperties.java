package com.roshine.security.props;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2023-10-31 21:25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class SsoProperties {

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "客户端秘钥")
    private String clientSecret;

    @Schema(description = "ado-sso地址前缀")
    private String adpSsoUriPrefix;

    @Schema(description = "放行的uri地址")
    private String[] excludePath;
}
