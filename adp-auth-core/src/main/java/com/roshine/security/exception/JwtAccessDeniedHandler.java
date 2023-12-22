package com.roshine.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roshine.adp.base.client.response.Response;
import com.roshine.adp.base.client.response.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-13 21:47
 * @Description 没有访问权限
 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Response<Object> response = Response.<Object>init(httpServletResponse)
                .setCode(ResultCode.NOT_ACCESS_PERMISSION)
                .setMessage(ResultCode.NOT_ACCESS_PERMISSION_MESSAGE)
                .setBody(e.getMessage());
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }
}
