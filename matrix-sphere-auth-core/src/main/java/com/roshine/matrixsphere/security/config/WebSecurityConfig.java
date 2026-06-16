package com.roshine.matrixsphere.security.config;

import com.roshine.matrixsphere.base.client.login.LoginAccount;
import com.roshine.matrixsphere.base.client.login.LoginInfo;
import com.roshine.matrixsphere.base.client.login.LoginInfoHelper;
import com.roshine.matrixsphere.security.handler.SsoAccessDeniedHandler;
import com.roshine.matrixsphere.security.handler.SsoAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author roshine
 * @version 2.0.0 (DDD & Spring Security 6.x Refactored)
 * 微服务全局安全与鉴权核心配置 (Resource Server)
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final SsoAuthenticationEntryPoint authenticationEntryPoint;
    private final SsoAccessDeniedHandler accessDeniedHandler;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WebSecurityConfig(
            SsoAuthenticationEntryPoint authenticationEntryPoint,
            SsoAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * 核心安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF (微服务基于 Token，不需要防范 CSRF)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 开启跨域支持 (自动引用 WebConfig 中配置的 corsConfigurationSource)
                .cors(Customizer.withDefaults())

                // 3. 禁用 Session (拥抱纯粹的无状态微服务)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 配置 API 拦截规则
                .authorizeHttpRequests(authorize -> authorize
                        // 核心白名单放行 (如 Swagger, Actuator 健康检查)
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/**",
                                "/error").permitAll()
                        // 其他所有请求必须携带合法的 Token
                        .anyRequest().authenticated()
                )

                // 5. 核心魔法：开启 OAuth2 资源服务器，使用不透明令牌 (Opaque Token) 校验
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(Customizer.withDefaults()) // 自动读取 yaml 中的 introspection-uri
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // 6. 异常兜底：统一返回 401 和 403 的 JSON 格式
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // 7. 植入微服务上下文透传过滤器 (非常重要：必须放在 Spring 解析完 Token 之后)
                .addFilterAfter(new LoginInfoContextFilter(), org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 内部组件：企业级登录上下文自动透传过滤器
     * 核心职责：将 Security 解析好的 Authentication 转换为业务层的 LoginInfo，并防止内存泄漏
     */
    public static class LoginInfoContextFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            // 从 Spring Security 的上下文中抓取当前的认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 如果是合法的 OAuth2 Token 访问
            if (authentication instanceof BearerTokenAuthentication bearerAuth) {
                OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) bearerAuth.getPrincipal();

                // 组装我们在 client 层定义的业务级 LoginAccount (这些属性由 SSO 的 Introspection 接口返回)
                LoginAccount account = new LoginAccount()
                        // 假设 SSO 返回的 Claims 中包含了以下字段，可根据实际情况调整 Key 的名字
                        .setId(getAttributeAsString(principal, "userId", authentication.getName()))
                        .setAccountNo(getAttributeAsString(principal, "accountNo", authentication.getName()))
                        .setAccountName(getAttributeAsString(principal, "accountName", authentication.getName()))
                        .setPhone(getAttributeAsString(principal, "phone", ""));

                // 组装并注入到 ThreadLocal
                LoginInfo loginInfo = new LoginInfo()
                        .setAccount(account)
                        .setTokenValue(bearerAuth.getToken().getTokenValue());

                LoginInfoHelper.setUserInfo(loginInfo);
            }

            try {
                // 放行请求，继续执行您的业务 Controller
                filterChain.doFilter(request, response);
            } finally {
                // 【架构红线】：请求处理完毕，必须清理 ThreadLocal，防止 Tomcat 线程复用导致的越权与内存泄漏！
                LoginInfoHelper.removeUserInfo();
            }
        }

        private String getAttributeAsString(OAuth2AuthenticatedPrincipal principal, String key, String defaultValue) {
            Object value = principal.getAttribute(key);
            return value != null ? value.toString() : defaultValue;
        }
    }
}