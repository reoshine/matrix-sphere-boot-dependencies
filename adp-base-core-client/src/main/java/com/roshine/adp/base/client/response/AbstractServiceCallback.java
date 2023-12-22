package com.roshine.adp.base.client.response;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 17:28
 * @Description
 */
public abstract class AbstractServiceCallback<T> {

    /**
     * 用于处理服务请求、返回值
     * @param response
     * @return
     * @throws Exception
     */
    public abstract ServiceResponse<T> doInOperation(ServiceResponse<T> response) throws Exception;
}
