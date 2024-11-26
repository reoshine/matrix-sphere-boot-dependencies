package com.roshine.matrixsphere.security.domain;

import com.roshine.matrixsphere.base.client.response.ServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2023-09-05 22:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IOAuth2RegisteredClientRequest extends ServiceRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -9200051692133323777L;

    @Schema(defaultValue = "客户端id")
    @NotBlank(message = "客户端id不可为空")
    private String clientId;
}
