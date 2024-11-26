package com.roshine.matrixsphere.security.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.roshine.matrixsphere.base.core.utils.RedisUtils;
import com.roshine.matrixsphere.security.constant.SecurityConstant;
import com.roshine.matrixsphere.security.domain.OAuth2RegisteredClientDTO;
import com.roshine.matrixsphere.security.exception.SsoAuthenticationEntryPoint;
import com.roshine.matrixsphere.security.handler.SsoAccessDeniedHandler;
import com.roshine.matrixsphere.security.props.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-10-13 21:47
 */
@EnableWebSecurity
@Configuration
@Order(-1)
public class WebSecurityConfig {

    private final SsoProperties ssoProperties;

    @Value("${spring.application.name}")
    private String clientId;

    @Autowired
    public WebSecurityConfig(SsoProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }

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
        String s = RedisUtils.get(SecurityConstant.CACHE_TOKEN_PREFIX + SecurityConstant.REGISTERED_CLIENT);
        List<OAuth2RegisteredClientDTO> registeredClientDTOList = JSONUtil.toList(s, OAuth2RegisteredClientDTO.class);
        Map<String, String> registeredClientMap = registeredClientDTOList.parallelStream().collect(Collectors.toMap(OAuth2RegisteredClientDTO::getClientId, OAuth2RegisteredClientDTO::getClientSecret, (k1, k2) -> k1));
        if (!registeredClientMap.containsKey(clientId)) {
            throw new IllegalArgumentException(StrUtil.format("不存在此客户: {}", clientId));
        }
        String clientSecret = registeredClientMap.get(clientId);
        return new SpringOpaqueTokenIntrospector(ssoProperties.getAdpSsoUriPrefix()  + SecurityConstant.OAUTH_INTROSPECT, clientId, clientSecret);
    }
}
