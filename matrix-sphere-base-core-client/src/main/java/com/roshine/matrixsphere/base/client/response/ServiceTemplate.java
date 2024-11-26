package com.roshine.matrixsphere.base.client.response;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-31 17:25
 * @Description
 */
@Component("serviceTemplate")
@Slf4j
public class ServiceTemplate {

    private final static ValidatorFactory GLOBAL_VALIDATOR = Validation.buildDefaultValidatorFactory();

    /**
     * 执行客户端请求
     * @param v 客户端参数
     * @param action 自定义处理
     * @param classes 请求参数类型
     * @return ServiceResponse
     */
    public <V extends ServiceRequest, T> ServiceResponse<T> execute(@Nonnull V v, AbstractServiceCallback<T> action, Class<?>... classes){
        SerialNumberHelper.setSerialNumber(v.getSerialNumber());
        try {
            // 执行参数验证
            ServiceResponse<T> response = new ServiceResponse<>();
            String result = this.validation(v, classes);
            if (StringUtils.isNotBlank(result)) {
                // 返回参数验证结果
                response.setCode(ResultCode.PARAM_VALIDATOR_ERROR);
                response.setMessage(result);
                return response;
            }
            return action.doInOperation(response);
        }catch (Exception e) {
            // 返回参数验证结果
            ServiceResponse<T> response = new ServiceResponse<>();
            response.setCode(ResultCode.PARAM_VALIDATOR_ERROR);
            response.setMessage(ResultCode.PARAM_VALIDATOR_ERROR_MESSAGE);
            log.error("校验参数异常：", e);
            return response;
        }finally {
            SerialNumberHelper.remove();
        }
    }

    /**
     * 执行客户端请求
     * @param v 客户顿参数
     * @param action 自定义处理
     * @param <V> 请求参数类型
     * @return
     */
    public <V extends ServiceRequest, T> ServiceResponse<T> execute(@Nonnull V v, Function<V, T> action){
        SerialNumberHelper.setSerialNumber(v.getSerialNumber());
        try {
            // 执行参数验证
            ServiceResponse<T> response = new ServiceResponse<>();
            String result = this.validation(v);
            if (StringUtils.isNotBlank(result)) {
                // 返回参数验证结果
                response.setCode(ResultCode.PARAM_VALIDATOR_ERROR);
                response.setMessage(result);
                return response;
            }
            return response.setBody(action.apply(v));
        }catch (Exception e) {
            // 返回参数验证结果
            ServiceResponse<T> response = new ServiceResponse<>();
            response.setCode(ResultCode.PARAM_VALIDATOR_ERROR);
            response.setMessage(ResultCode.PARAM_VALIDATOR_ERROR_MESSAGE);
            return response;
        }finally {
            SerialNumberHelper.remove();
        }
    }

    public <V extends ServiceRequest> String validation(V v, Class<?>...classes) {
        Validator validator = GLOBAL_VALIDATOR.getValidator();
        Set<ConstraintViolation<V>> set = validator.validate(v, classes);
        List<TempResultDO<String, String>> list = new ArrayList<>(4);
        if (!set.isEmpty()) {
            for (ConstraintViolation<V> cv : set) {
                TempResultDO<String, String> temp = new TempResultDO<>();
                temp.setKey(StrUtil.isNotBlank(cv.getPropertyPath().toString()) ? cv.getPropertyPath().toString() : "提示");
                temp.setValue(cv.getMessage());
                list.add(temp);
            }
        }
        return list.isEmpty() ? null : JSONUtil.toJsonStr(list);
    }

    @Getter
    static
    class TempResultDO<A, B> {
        private A key;
        private B value;

        public void setKey(A key) {
            this.key = key;
        }

        public void setValue(B value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TempResultDO{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
