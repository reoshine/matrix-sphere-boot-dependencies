package com.roshine.matrixsphere.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roshine.matrixsphere.base.client.response.Response;
import com.roshine.matrixsphere.base.client.response.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-03-21 10:59
 * 没有携带token或者token无效
 */
public class SsoAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Response<Object> result = Response.init()
                .setCode(ResultCode.NOT_ACCESS_PERMISSION)
                .setMessage(ResultCode.NOT_ACCESS_PERMISSION_MESSAGE)
                .setBody(authException.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
