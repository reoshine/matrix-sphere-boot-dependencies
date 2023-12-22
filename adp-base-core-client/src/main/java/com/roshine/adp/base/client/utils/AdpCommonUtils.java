package com.roshine.adp.base.client.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-13 21:33
 * @Description 应用统一工具类
 */
public class AdpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(AdpCommonUtils.class);

    /**
     * 间隔符
     */
    private static final String SEPERATOR = ":";

    /**
     * 将java对象转换成json字符串
     *
     * @param obj 准备转换的对象
     * @return json字符串
     */
    public static String beanToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json 准备转换的json字符串
     * @return Object
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将一个Object强转为T<br/>
     *
     * @param object 对象
     * @param <T>    泛型类型
     * @return 强转后的T类型对象
     */
    public static <T> T covert(Object object) {
        return (T) object;
    }

    /**
     * 格式化一个带有占位符的字符串<br/>
     *
     * @param message 原始字符串,占位符通过{}标记
     * @param args    占位符替换值
     * @return 格式化后的字符串
     */
    public static String format(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    /**
     * 获得一个不带间隔符的UUID
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 获得一个带间隔符小写的UUID
     *
     * @return
     */
    public static String getUuidToken() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    /***
     * 判断对象的属性是否全为空 需要排除serialVersionUID
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Boolean isAllFieldNull(T obj) {
        boolean result = true;
        if (obj != null) {
            List<Field> fields = new ArrayList<>();
            Class<?> tmpClass = obj.getClass();
            while (tmpClass != null) {
                fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
                tmpClass = tmpClass.getSuperclass();
            }

            if (CollUtil.isNotEmpty(fields)) {
                try {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if ("serialVersionUID".equals(field.getName())) {
                            continue;
                        }
                        if (field.get(obj) != null) {
                            if (field.getType() != String.class || String.valueOf(field.get(obj)) == null) {
                                result = false;
                                break;
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    logger.error("判断对象是否为空出错", e);
                }
            }
        }
        return result;
    }

    /**
     * 产生key
     *
     * @param keys
     * @return
     */
    public static String produceKey(String... keys) {
        StringBuilder key = new StringBuilder();
        if (keys.length == 1) {
            key = new StringBuilder(keys[0]);
        } else {
            for (int i = 0; i < keys.length - 1; i++) {
                key.append(keys[i]).append(SEPERATOR);
            }
            key.append(keys[keys.length - 1]);
        }
        return key.toString();
    }

    public static String getRequestJsonParam(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream inputStream = request.getInputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return String.valueOf(result);
    }
}
