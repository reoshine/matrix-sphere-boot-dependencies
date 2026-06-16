package com.roshine.matrixsphere.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author roshine
 * @version 2.0.0
 * 全局跨域 (CORS) 基础配置
 */
@Configuration
public class WebConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许任何域名跨域访问 (在生产环境建议替换为具体的业务域名集合)
        configuration.setAllowedOriginPatterns(List.of("*"));
        // 允许任何请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 允许任何请求方法 (GET, POST, PUT, DELETE 等)
        configuration.setAllowedMethods(List.of("*"));
        // 允许携带 Cookie/凭证 (极为关键，配合 OriginPatterns 使用)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口生效
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}