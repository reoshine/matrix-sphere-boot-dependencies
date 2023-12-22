package com.roshine.adp.base.client.exception;


import com.roshine.adp.base.client.response.ResultCode;
import com.roshine.adp.base.client.utils.AdpCommonUtils;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-02 21:08
 * @Description
 */
public class Asserts {
    public static void assertTrue(boolean condition, String message) {
        asserts(condition, ResultCode.ASSERT_EXCEPTION, null, message);
    }

    public static void assertTrue(boolean condition, String format, Object...args) {
        asserts(condition, ResultCode.ASSERT_EXCEPTION, null, format, args);
    }

    public static void assertTrue(boolean condition, int code, String message) {
        asserts(condition, code, null, message);
    }

    public static void assertTrue(boolean condition, int code, String format, Object...args) {
        asserts(condition, code, null, format, args);
    }

    public static void assertTrue(boolean condition, int code, Throwable cause, String message) {
        asserts(condition, code, cause, message);
    }

    public static void assertTrue(boolean condition, int code, Throwable cause, String format, String message) {
        asserts(condition, code, cause, format, message);
    }

    private static void asserts(boolean condition, Integer code, Throwable cause, String message, Object...args) {
        if (!condition) {
            message = AdpCommonUtils.format(message, args);
            final StackTraceElement methodCaller = Thread.currentThread().getStackTrace()[2];
            String log = "\nBusiness Exception: {" +
                    "\n\tServices Name: " + methodCaller.getClassName() +
                    "\n\tMethod Name: " + methodCaller.getMethodName() +
                    "\n\tLinenumber: " + methodCaller.getLineNumber() +
                    "\n\tService Error Code: " + code +
                    "\n\tService Error Message: " + message +
                    "\n}\n";
            throw new AssertException(code, message, log, cause);
        }
    }
}
