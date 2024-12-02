package com.roshine.matrixsphere.base.client.response;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-26 23:45
 * 定义抽象结果处理
 */
public abstract class AbstractResponseCallback<T> {

    /**
     * 成功结果处理
     * @param response 响应体
     * @return Response<T>
     */
    public abstract Response<T> success(Response<T> response);

    /**
     * 失败结果处理
     * @param response 响应体
     * @return Response<T>
     */
    public Response<T> fail(Response<T> response) {
        return response;
    }
}
