package com.roshine.adp.base.client.utils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * servlet工具类
 */
@Slf4j
public class ServletUtils {

    /**
     * 获取RequestAttributes对象
     * @return
     */
    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes)attributes;
    }

    /**
     * 获取request对象
     * @return
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response对象
     * @return
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session对象
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 把字符串打印到客户端
     * @param string
     * @return
     */
    public static String sendString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            log.error("打印信息到客户端异常，原因: {}", e.getMessage());
        }
        return null;
    }
}
