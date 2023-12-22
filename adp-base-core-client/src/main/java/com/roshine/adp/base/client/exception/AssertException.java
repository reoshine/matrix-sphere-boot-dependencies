package com.roshine.adp.base.client.exception;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-02 21:20
 * @Description
 */
public class AssertException extends RuntimeException {

    private static final long serialVersionUID = -6371172563056231667L;

    private int code;

    private String message;

    public AssertException(int code, String message, String error, Throwable cause) {
        super(error, cause);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
