package com.roshine.adp.base.client.response;

import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 16:49
 * @Description
 */
public class ServiceResponse<T> implements Serializable {

    private static final long serialVersionUID = 1874805748892783045L;

    private int code = ResultCode.SUCCESS;

    private String message = "success";

    private T body;

    private String serviceSource;

    public ServiceResponse<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public String getServiceSource() {
        return serviceSource;
    }

    public void setServiceSource(String serviceSource) {
        this.serviceSource = serviceSource;
    }
}
