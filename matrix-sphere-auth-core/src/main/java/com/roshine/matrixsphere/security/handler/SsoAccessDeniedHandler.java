package com.roshine.matrixsphere.security.handler;

import cn.hutool.json.JSONUtil;
import com.roshine.matrixsphere.base.client.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author roshine
 * @version 2.0.0
 * 全局权限不足拦截器 (HTTP 403)
 * 核心职责：当用户已登录，但缺少访问某个接口所需的 Role/Authority 时触发。
 */
@Slf4j
@Component
public class SsoAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("访问受限 (无权限) URI: {}，原因: {}", request.getRequestURI(), accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Response<Void> res = Response.fail(403, "抱歉，您没有权限访问该资源");
        response.getWriter().write(JSONUtil.toJsonStr(res));
    }
}