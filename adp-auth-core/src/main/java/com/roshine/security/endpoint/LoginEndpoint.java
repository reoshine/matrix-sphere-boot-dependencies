package com.roshine.security.endpoint;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.roshine.adp.base.client.response.RPCServiceHelper;
import com.roshine.adp.security.client.domain.dto.OAuth2RegisteredClientDTO;
import com.roshine.adp.security.client.domain.req.IOAuth2RegisteredClientRequest;
import com.roshine.adp.security.client.service.RpcAuthClientDetailsService;
import com.roshine.security.constant.SecurityConstant;
import com.roshine.security.props.SsoProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 1.0.0
 * @date 2023-09-04 21:27
 */
@Slf4j
@Controller
public class LoginEndpoint {

    @Autowired
    private SsoProperties ssoProperties;

    @Autowired
    @DubboReference(group = "adp-sso-service", version = "1.0.0")
    private RpcAuthClientDetailsService rpcAuthClientDetailsService;

    @GetMapping(SecurityConstant.CLIENT_LOGIN_URI)
    public RedirectView clientLogin() {
        IOAuth2RegisteredClientRequest request = new IOAuth2RegisteredClientRequest();
        request.setClientId(ssoProperties.getClientId());
        OAuth2RegisteredClientDTO registeredClientDTO = RPCServiceHelper.handleResponse(rpcAuthClientDetailsService.loadClientByClientId(request));
        String uri = registeredClientDTO.getRedirectUris().stream().map(item -> "redirect_uri=" + item).collect(Collectors.joining("&"));
        String template = ssoProperties.getAdpSsoUriPrefix()  + SecurityConstant.OAUTH_AUTHORIZE_URL + "?response_type=code&scope={}&client_id={}&state={}&{}";
        String authorizeUrl = StrUtil.format(template,
                "message.read",
                registeredClientDTO.getClientId(),
                "ok",
                uri);
        log.info("认证地址: {}", authorizeUrl);
        return new RedirectView(authorizeUrl);
    }

    @GetMapping(value = SecurityConstant.LOGIN_CALLBACK_URI)
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IOAuth2RegisteredClientRequest clientRequest = new IOAuth2RegisteredClientRequest();
        clientRequest.setClientId(ssoProperties.getClientId());
        OAuth2RegisteredClientDTO registeredClientDTO = RPCServiceHelper.handleResponse(rpcAuthClientDetailsService.loadClientByClientId(clientRequest));
        String code = request.getParameter("code");
        Assert.isTrue(StrUtil.isNotBlank(code), "授权码不存在");
        log.info("authCode = {}", code);
        Map<String, Object> param = new HashMap<>(8);
        param.put("grant_type", "authorization_code");
        param.put("redirect_uri", registeredClientDTO.getRedirectUris().parallelStream().findFirst().orElse(null));
        param.put("code", code);
        String url = ssoProperties.getAdpSsoUriPrefix() + SecurityConstant.OAUTH_TOKEN_URL;
        String body = HttpRequest.post(url)
                .basicAuth(registeredClientDTO.getClientId(), ssoProperties.getClientSecret())
                .form(param)
                .execute()
                .body();
        JSONObject parseObj = JSONUtil.parseObj(body);
        String accessToken = parseObj.getStr(SecurityConstant.ACCESS_TOKEN);
        String refreshToken = parseObj.getStr(SecurityConstant.REFRESH_TOKEN);
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);
        response.sendRedirect(registeredClientDTO.getSuccessRedirectUri() + "?accessToken=" + accessToken);
    }
}
