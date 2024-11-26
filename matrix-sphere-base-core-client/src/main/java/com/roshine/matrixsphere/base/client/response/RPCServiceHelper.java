package com.roshine.matrixsphere.base.client.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 16:54
 * @Description
 */
public class RPCServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(RPCServiceHelper.class);

    /**
     * 用于处理hsf返回值
     * @param response
     * @param <T>
     * @return
     */
    public static <T> T handleResponse(ServiceResponse<T> response) {
        return response.getBody();
    }

    public static <T, R extends ServiceRequest> T handleResponse(R r, Function<R, ServiceResponse<T>> execute) {
        // 此处可对R进行预处理，比如统一传递用户信息
        r.setSerialNumber(SerialNumberHelper.getSerialNumber());
        ServiceResponse<T> result = execute.apply(r);
        return result.getBody();
    }


}
