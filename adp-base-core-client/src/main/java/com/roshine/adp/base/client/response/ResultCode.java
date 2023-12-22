package com.roshine.adp.base.client.response;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-26 22:40
 * @Description
 */
public class ResultCode {

    /**
     * 成功处理返回
     */
    public static final int SUCCESS = 2000;
    public static final String SUCCESS_MESSAGE = "success";

    /**
     * 程序运行异常信息
     */
    public static final int ERROR = 4000;
    public static final String ERROR_MESSAGE = "error message: [{}]";

    /**
     * 参数校验错误返回
     */
    public static final int PARAM_VALIDATOR_ERROR = 4001;
    public static final String PARAM_VALIDATOR_ERROR_MESSAGE = "param validator error: {}";

    /**
     * 未登录返回
     */
    public static final int NOT_LOGIN_ERROR = 4002;
    public static final String NOT_LOGIN_ERROR_MESSAGE = "not login";

    /**
     * 没有访问权限
     */
    public static final int NOT_ACCESS_PERMISSION = 4003;
    public static final String NOT_ACCESS_PERMISSION_MESSAGE = "not access permission";

    /**
     * 重复提交错误
     */
    public static final int REPEAT_SUBMIT_ERROR = 4005;

    /**
     * 参数格式错误
     */
    public static final int PARAM_FORMAT = 4006;

    /**
     * 断言异常
     */
    public static final int ASSERT_EXCEPTION = 4007;

    /**
     * 没有菜单权限
     */
    public static final int NO_MENU_PERMISSION_EXCEPTION = 4008;
}
