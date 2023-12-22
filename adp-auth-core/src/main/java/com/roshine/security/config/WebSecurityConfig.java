package com.roshine.security.config;

import com.roshine.security.constant.SecurityConstant;
import com.roshine.security.exception.SsoAuthenticationEntryPoint;
import com.roshine.security.handler.SsoAccessDeniedHandler;
import com.roshine.security.props.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-13 21:47
 * @Description
 */
@EnableWebSecurity
@Configuration
@Order(-1)
public class WebSecurityConfig {

    @Autowired
    private SsoProperties ssoProperties;

    /**
     * 配置认证相关的过滤器链
     *
     * @param http spring security核心配置类
     * @return 过滤器链
     * @throws Exception 抛出
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(ssoProperties.getExcludePath())
                        .permitAll().anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth -> oauth.opaqueToken(Customizer.withDefaults())
                        .accessDeniedHandler(new SsoAccessDeniedHandler())
                        .authenticationEntryPoint(new SsoAuthenticationEntryPoint()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(ssoProperties.getExcludePath())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SpringOpaqueTokenIntrospector opaqueTokenIntrospector() {
        return new SpringOpaqueTokenIntrospector(ssoProperties.getAdpSsoUriPrefix()  + SecurityConstant.OAUTH_INTROSPECT, SecurityConstant.CLIENT_ID, SecurityConstant.CLIENT_SECRET);
    }
}
