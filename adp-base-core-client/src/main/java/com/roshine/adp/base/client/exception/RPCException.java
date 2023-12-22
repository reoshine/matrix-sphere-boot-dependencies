package com.roshine.adp.base.client.exception;

import cn.hutool.core.text.StrBuilder;

import java.io.Serial;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 17:09
 * @Description
 */
public class RPCException extends Exception {

    @Serial
    private static final long serialVersionUID = 3463905048300515582L;

    private static final String lineSeparator = System.getProperty("line.separator");

    private final String exceptionCode;

    private final String desc;

    public RPCException(String exceptionCode) {
        this(exceptionCode, "");
    }

    public RPCException(String exceptionCode, String desc) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.desc = desc;
    }

    public RPCException(String exceptionCode, String desc, Throwable t) {
        super(desc, t);
        this.exceptionCode = exceptionCode;
        this.desc = desc;
    }

    public RPCException(String exceptionCode, Throwable t) {
        this(exceptionCode, "", t);
    }

    @Override
    public String toString() {
        if (desc == null) {
            return exceptionCode;
        }

        StrBuilder sb = new StrBuilder(exceptionCode);
        sb.append(lineSeparator);
        sb.append("Desc info:").append(desc);
        return sb.toString();
    }
}
