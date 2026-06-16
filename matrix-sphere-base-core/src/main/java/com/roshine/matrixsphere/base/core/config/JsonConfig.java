package com.roshine.matrixsphere.base.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author roshine
 * @version 2.0.0
 * 全局 JSON 序列化定制器 (不破坏 Spring Boot 自动装配)
 */
@Configuration
public class JsonConfig {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 设置时区与基础格式
            builder.timeZone(TimeZone.getTimeZone("GMT+8"));
            builder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT);

            // 注册针对特定类型的自定义序列化器
            builder.serializerByType(BigDecimal.class, BigDecimalStringSerializer.instance);
            builder.serializerByType(Instant.class, new CustomInstantSerializer());

            // Java 8 Time 等模块已被 Spring Boot 自动注册，无需手工 addSerializer
        };
    }

    /**
     * Instant 定制序列化器
     */
    private static class CustomInstantSerializer extends JsonSerializer<Instant> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)
                .withZone(ZoneId.of("Asia/Shanghai"));
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(FORMATTER.format(value));
            }
        }
    }
}