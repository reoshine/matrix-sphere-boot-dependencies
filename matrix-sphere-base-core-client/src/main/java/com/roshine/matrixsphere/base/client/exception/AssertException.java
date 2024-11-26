package com.roshine.matrixsphere.base.client.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-02 21:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssertException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6371172563056231667L;

    @Getter
    private int code;

    private String message;

    public AssertException(int code, String message, String error, Throwable cause) {
        super(error, cause);
        this.code = code;
        this.message = message;
    }
}
