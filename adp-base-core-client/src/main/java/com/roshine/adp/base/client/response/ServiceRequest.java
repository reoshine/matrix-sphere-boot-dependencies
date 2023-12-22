package com.roshine.adp.base.client.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 16:57
 * @Description
 */
@Getter
public class ServiceRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2697313520626603114L;

    @JsonIgnore
    @Schema(hidden = true)
    private String beUsedSource;

    @JsonIgnore
    @Schema(hidden = true)
    private String serialNumber;

    public ServiceRequest() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        if (stackTrace != null && stackTrace.length > 2) {
            StackTraceElement stackTraceElement = stackTrace[2];
            this.beUsedSource = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
        }
    }

    public void setBeUsedSource(String beUsedSource) {
        this.beUsedSource = beUsedSource;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
