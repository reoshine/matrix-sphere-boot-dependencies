package com.roshine.matrixsphere.base.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2022-07-06 22:05
 */
@Configuration
public class JsonConfig {

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_DATE_TIME = DateTimeFormatter
            .ofPattern(DEFAULT_DATE_TIME_FORMAT).withZone(ZoneId.of("Asia/Shanghai"));

    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter DEFAULT_DATE = DateTimeFormatter
            .ofPattern(DEFAULT_DATE_FORMAT).withZone(ZoneId.of("Asia/Shanghai"));

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_TIME = DateTimeFormatter
            .ofPattern(DEFAULT_TIME_FORMAT).withZone(ZoneId.of("Asia/Shanghai"));




    @Bean
    public ObjectMapper timeJackson2ObjectMapperBuilder() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        // 日期类型字符串处理
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT));
        // 序列化枚举是以toString()来输出
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //注册模块，支持多模块注册
        objectMapper.registerModule(javaTimeModule());
        objectMapper.registerModule(bigDecimalModule());
        return objectMapper;
    }

    /**
     * java8日期处理
     * @return JavaTimeModule
     */
    private JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DEFAULT_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DEFAULT_DATE_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DEFAULT_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DEFAULT_DATE));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DEFAULT_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DEFAULT_TIME));
        javaTimeModule.addSerializer(Instant.class, new CustomInstantSerializer());
        return javaTimeModule;
    }

    /**
     * BigDecimal 处理
     * @return SimpleModule
     */
    private SimpleModule bigDecimalModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, BigDecimalStringSerializer.instance);
        simpleModule.addKeySerializer(BigDecimal.class, BigDecimalStringSerializer.instance);
        return simpleModule;
    }

    private static class CustomInstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.atZone(ZoneId.of("Asia/Shanghai")).format(DEFAULT_DATE_TIME));
        }
    }
}
