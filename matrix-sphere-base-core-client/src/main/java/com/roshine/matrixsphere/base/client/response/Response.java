package com.roshine.matrixsphere.base.client.response;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roshine.matrixsphere.base.client.utils.CommonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-26 22:35
 * @Description
 */
@Data
@Accessors(chain = true)
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 409142379120820015L;

    private int code = ResultCode.SUCCESS;

    private String message = ResultCode.SUCCESS_MESSAGE;

    private T body;

    @JsonIgnore
    private BindingResult bindingResult;

    private Response(){}

    /**
     * 初始化一个统一的返回对象
     * @param <T>
     * @return
     */
    public static <T> Response<T> init() {
        return new Response<>();
    }

    /**
     * 初始化一个统一的返回对象
     * @param <T>
     * @return
     */
    public static <T> Response<T> init(T body) {
        return new Response<T>().setBody(body);
    }

    /**
     * 初始化一个统一的返回对象
     * @param <T>
     * @return
     */
    public static <T> Response<T> init(BindingResult result) {
        return new Response<T>().setBindingResult(result);
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 初始化一个统一的对象并执行参数校验, 如果参数校验有问题将不执行action
     * @param request
     * @param action
     * @param <V>
     * @param <T>
     * @return
     */
    public static <V, T> Response<T> execute(@NotNull V request, Function<V, T> action) {
        Response<T> response = new Response<>();
        Set<ConstraintViolation<V>> set = validator.validate(request);
        if (!set.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            for (ConstraintViolation<V> cv : set) {
                builder.append("{")
                        .append("\"key\":\"").append(StrUtil.blankToDefault(cv.getPropertyPath().toString(), "提示"))
                        .append("\",")
                        .append("\"value\":\"").append(cv.getMessage()).append("\"")
                        .append("},");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("]");
            return response.failed(ResultCode.PARAM_VALIDATOR_ERROR, builder.toString());
        }
        return response.setBody(action.apply(request));
    }

    /**
     * 初始化一个统一的对象并执行参数校验, 如果参数校验有问题将不执行action
     * @param request
     * @param action
     * @param after
     * @param <V>
     * @param <T>
     * @return
     */
    public static <V, T> Response<T> execute(@NotNull V request, Function<V, T> action, Function<T, T> after) {
        Response<T> result = execute(request, action);
        if (ResultCode.PARAM_VALIDATOR_ERROR != result.getCode() && Objects.nonNull(result.getBody())) {
            result.setBody(after.apply(result.getBody()));
        }
        return result;
    }

    /**
     * 与init(BindingResult result)函数配套使用, 判断参数验证如果有问题将不执行callback success逻辑
     * @param callback
     * @return
     */
    public Response<T> handle(AbstractResponseCallback<T> callback) {
        this.doValidate();
        if (this.code == ResultCode.PARAM_VALIDATOR_ERROR) {
            return callback.fail(this);
        }else {
            return callback.success(this);
        }
    }

    /**
     * 执行验证
     */
    public void doValidate() {
        if (this.bindingResult != null && this.bindingResult.getErrorCount() > 0) {
            this.setCode(ResultCode.PARAM_VALIDATOR_ERROR);
            StringBuffer builder = new StringBuffer();
            builder.append("[");
            if (this.bindingResult.getFieldErrorCount() > 0) {
                for (FieldError error : bindingResult.getFieldErrors()) {
                    builder.append("{")
                            .append("\"key\":\"").append(error.getField()).append("\",")
                            .append("\"value\":\"").append(error.getDefaultMessage()).append("\"")
                            .append("},");
                }
            }
            if (this.bindingResult.getGlobalErrorCount() > 0) {
                for (ObjectError error : this.bindingResult.getGlobalErrors()) {
                    builder.append("{")
                            .append("\"key\":\"提示\",")
                            .append("\"value\":\"").append(error.getDefaultMessage()).append("\"")
                            .append("},");
                }
            }
            builder = new StringBuffer(builder.substring(0, builder.toString().length() - 1));
            builder.append("]");
            this.setMessage(CommonUtils.format(ResultCode.PARAM_VALIDATOR_ERROR_MESSAGE, builder.toString()));
        }
    }

    /**
     * 初始化后使用设置一个详细的失败信息
     * @param code
     * @param message
     * @param body
     * @return
     */
    public Response<T> failed(int code, String message, T body) {
        return this.setCode(code).setMessage(message).setBody(body);
    }

    /**
     * 初始化后使用设置一个详细的失败信息
     * @param code
     * @param message
     * @return
     */
    public Response<T> failed(int code, String message) {
        return this.failed(code, message, null);
    }

    /**
     * 初始化后使用设置一个详细的失败信息
     * @param message
     * @return
     */
    public Response<T> failed(String message) {
        return this.failed(ResultCode.ERROR, message, null);
    }
}
