package com.roshine.matrixsphere.security.endpoint;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.roshine.matrixsphere.base.core.utils.RedisUtils;
import com.roshine.matrixsphere.security.constant.SecurityConstant;
import com.roshine.matrixsphere.security.domain.IOAuth2RegisteredClientRequest;
import com.roshine.matrixsphere.security.domain.OAuth2RegisteredClientDTO;
import com.roshine.matrixsphere.security.props.SsoProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    @Value("${spring.application.name}")
    private String clientId;

    @GetMapping(SecurityConstant.CLIENT_LOGIN_URI)
    public RedirectView clientLogin() {
        IOAuth2RegisteredClientRequest request = new IOAuth2RegisteredClientRequest();
        request.setClientId(ssoProperties.getClientId());

        String registeredClientStr = RedisUtils.get(SecurityConstant.CACHE_TOKEN_PREFIX + SecurityConstant.REGISTERED_CLIENT);
        List<OAuth2RegisteredClientDTO> registeredClientDTOList = JSONUtil.toList(registeredClientStr, OAuth2RegisteredClientDTO.class);
        Map<String, OAuth2RegisteredClientDTO> registeredClientMap = registeredClientDTOList.parallelStream().collect(Collectors.toMap(OAuth2RegisteredClientDTO::getClientId, a -> a, (k1, k2) -> k1));
        if (!registeredClientMap.containsKey(clientId)) {
            throw new IllegalArgumentException(StrUtil.format("不存在此客户: {}", clientId));
        }
        OAuth2RegisteredClientDTO registeredClient = registeredClientMap.get(clientId);
        String uri = registeredClient.getRedirectUris().stream().map(item -> "redirect_uri=" + item).collect(Collectors.joining("&"));
        String template = ssoProperties.getAdpSsoUriPrefix()  + SecurityConstant.OAUTH_AUTHORIZE_URL + "?response_type=code&scope={}&client_id={}&state={}&{}";
        String authorizeUrl = StrUtil.format(template,
                "message.read",
                clientId,
                "ok",
                uri);
        log.info("认证地址: {}", authorizeUrl);
        return new RedirectView(authorizeUrl);
    }

    @GetMapping(value = SecurityConstant.LOGIN_CALLBACK_URI)
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String registeredClientStr = RedisUtils.get(SecurityConstant.CACHE_TOKEN_PREFIX + SecurityConstant.REGISTERED_CLIENT);
        List<OAuth2RegisteredClientDTO> registeredClientDTOList = JSONUtil.toList(registeredClientStr, OAuth2RegisteredClientDTO.class);
        Map<String, OAuth2RegisteredClientDTO> registeredClientMap = registeredClientDTOList.parallelStream().collect(Collectors.toMap(OAuth2RegisteredClientDTO::getClientId, a -> a, (k1, k2) -> k1));
        if (!registeredClientMap.containsKey(clientId)) {
            throw new IllegalArgumentException(StrUtil.format("不存在此客户: {}", clientId));
        }
        OAuth2RegisteredClientDTO registeredClient = registeredClientMap.get(clientId);
        String code = request.getParameter("code");
        Assert.isTrue(StrUtil.isNotBlank(code), "授权码不存在");
        log.info("authCode = {}", code);
        Map<String, Object> param = new HashMap<>(8);
        param.put("grant_type", "authorization_code");
        param.put("redirect_uri", registeredClient.getRedirectUris().parallelStream().findFirst().orElse(null));
        param.put("code", code);
        String url = ssoProperties.getAdpSsoUriPrefix() + SecurityConstant.OAUTH_TOKEN_URL;
        String body = HttpUtil.createPost(url)
                .basicAuth(registeredClient.getClientId(), ssoProperties.getClientSecret())
                .form(param)
                .execute().body();
        JSONObject parseObj = JSONUtil.parseObj(body);
        String accessToken = parseObj.getStr(SecurityConstant.ACCESS_TOKEN);
        String refreshToken = parseObj.getStr(SecurityConstant.REFRESH_TOKEN);
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);
        response.sendRedirect(registeredClient.getSuccessRedirectUri() + "?accessToken=" + accessToken);
    }
}
