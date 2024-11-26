package com.roshine.matrixsphere.base.client.response;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 17:05
 * @Description
 */
public class SerialNumberHelper {
    private static ThreadLocal<String> local = new ThreadLocal<>();

    public static void setSerialNumber(String serialNumber) {
        local.set(serialNumber);
    }

    public static String getSerialNumber() {
        return local.get();
    }

    public static void remove() {
        local.remove();
    }
}
