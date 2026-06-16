package com.roshine.matrixsphere.base.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author roshine
 * @version 2.0.0
 * 统一 API 响应体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 核心：使用 Jackson 替代 Fastjson，当 data 为空时不参与序列化
public class Response<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Response<T> success() {
        return new Response<>(200, "操作成功", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(500, message, null);
    }

    public static <T> Response<T> fail(Integer code, String message) {
        return new Response<>(code, message, null);
    }
}