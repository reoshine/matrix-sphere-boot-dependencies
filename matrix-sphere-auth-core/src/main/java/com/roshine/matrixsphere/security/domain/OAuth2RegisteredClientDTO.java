package com.roshine.matrixsphere.security.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2023-10-30 21:09
 */
@Data
public class OAuth2RegisteredClientDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -27210646465884614L;

    private String clientId;

    private String clientSecret;

    private Set<String> scopes;

    private Set<String> redirectUris;

    private String successRedirectUri;
}
