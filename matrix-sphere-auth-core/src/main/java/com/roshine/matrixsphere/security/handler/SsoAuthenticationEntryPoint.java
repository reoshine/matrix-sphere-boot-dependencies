package com.roshine.matrixsphere.security.handler;

import cn.hutool.json.JSONUtil;
import com.roshine.matrixsphere.base.client.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author roshine
 * @version 2.0.0
 * 全局未认证拦截器 (HTTP 401)
 * 核心职责：当未携带 Token 或 Token 无效访问受保护资源时，返回统一的 JSON 格式。
 */
@Slf4j
@Component
public class SsoAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("访问受限 (未认证) URI: {}，原因: {}", request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 统一输出给前端
        Response<Void> res = Response.fail(401, "未登录或会话已过期，请重新登录");
        response.getWriter().write(JSONUtil.toJsonStr(res));
    }
}