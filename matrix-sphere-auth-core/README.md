# Matrix Sphere Auth Core

安全认证核心组件 — 基于 Spring Security 6.x 与 OAuth2.1 协议栈，为微服务提供统一的认证授权能力。

采用 **OAuth2 Resource Server（不透明令牌内省模式）**，无状态会话，适用于 Spring Cloud 微服务架构。

## 功能特性

- **OAuth2 资源服务器**：通过不透明令牌（Opaque Token）内省实现请求鉴权
- **无状态认证**：禁用 HTTP Session，每次请求独立鉴权
- **SSO 登录上下文透传**：将认证后的用户信息注入 `LoginInfoHelper` ThreadLocal，供业务层无感获取
- **CORS 跨域配置**：开箱即用的全局跨域支持
- **统一异常响应**：401 未认证 / 403 无权限，均以标准 `Response` JSON 格式返回
- **URL 白名单**：Knife4j API 文档、Actuator 健康检查等路径免认证

## 包结构

```
com.roshine.matrixsphere.security
├── config            # Security 配置
│   ├── WebConfig                 # CORS 跨域配置
│   └── WebSecurityConfig         # Spring Security 核心配置
└── handler           # 异常处理器
    ├── SsoAccessDeniedHandler    # 403 权限不足处理器
    └── SsoAuthenticationEntryPoint  # 401 未认证处理器
```

## 配置说明

### WebSecurityConfig 核心安全配置

```
┌─────────────────────────────────────────────────────────────────┐
│  SecurityFilterChain                                            │
│                                                                 │
│  ① CSRF  → 禁用（无状态微服务不需要）                               │
│  ② CORS  → 启用（使用 WebConfig 配置）                            │
│  ③ Session → STATELESS（不创建/使用 HTTP Session）               │
│  ④ URL 白名单 → /v3/api-docs/**, /swagger-ui/**,                │
│                  /actuator/**, /error                          │
│  ⑤ 其余请求 → 需要认证                                           │
│  ⑥ OAuth2 → opaqueToken（不透明令牌内省）                        │
│  ⑦ 异常处理 → 401/403 统一 JSON 响应                             │
│  ⑧ 自定义 Filter → 认证后将用户信息写入 ThreadLocal                │
└─────────────────────────────────────────────────────────────────┘
```

### 登录上下文注入流程

```
HTTP Request
    │
    ▼
BearerTokenAuthenticationFilter（Spring Security 内置）
    │ 提取 Authorization: Bearer xxx
    ▼
OpaqueToken 内省（调用授权服务器）
    │ 验证令牌 → 获取 OAuth2AuthenticatedPrincipal
    ▼
LoginInfoContextFilter（自定义 OncePerRequestFilter）
    │ 从 principal 属性中提取 userId / accountNo / accountName / phone
    ▼
LoginInfoHelper.setUserInfo(loginInfo) → ThreadLocal
    │
    ▼
业务层 → LoginInfoHelper.getUserId() / getUserInfo()
    │
    ▼
finally → LoginInfoHelper.removeUserInfo()（确保内存不泄漏）
```

### URL 白名单

以下路径无需认证即可访问：

| 路径                 | 说明                        |
|--------------------|---------------------------|
| `/v3/api-docs/**`  | Knife4j / Swagger API 文档  |
| `/swagger-ui/**`   | Knife4j UI 资源             |
| `/swagger-ui.html` | Swagger UI 入口             |
| `/actuator/**`     | Spring Boot Actuator 健康检查 |
| `/error`           | 错误兜底页面                    |

可按需在 `WebSecurityConfig` 中扩展白名单。

### CORS 配置

- 允许来源：`*`（所有来源）
- 允许请求头：`*`
- 允许 HTTP 方法：`*`
- 允许携带凭据：`true`
- 适用范围：所有路径 `/**`

## 异常响应格式

### 401 未认证

```json
{
  "code": 401,
  "message": "未登录或会话已过期，请重新登录"
}
```

### 403 无权限

```json
{
  "code": 403,
  "message": "抱歉，您没有权限访问该资源"
}
```

## 使用示例

### 业务层获取当前登录用户

```java
// 直接从 ThreadLocal 获取
String userId = LoginInfoHelper.getUserId();
LoginInfo loginInfo = LoginInfoHelper.getUserInfo();
LoginAccount account = loginInfo.getAccount();
```

### 在 Controller 中获取

```java
@GetMapping("/user/info")
public Response<UserInfoVO> getUserInfo() {
    String userId = LoginInfoHelper.getUserId();
    // 业务逻辑...
    return Response.success(userInfoVO);
}
```

### 异步线程中传递上下文

异步线程池默认启用 `ContextCopyingDecorator`，自动将 `LoginInfo` 和 MDC 从主线程透传到子线程：

```java
@Async("matrixAsyncExecutor")
public void asyncMethod() {
    // LoginInfoHelper.getUserId() 自动继承主线程的登录上下文
}
```

## 依赖

- `matrix-sphere-base-core`（内部模块）
- Spring Boot Starter Security
- Spring Boot Starter OAuth2 Client
- Spring Boot Starter OAuth2 Resource Server
- Lombok（provided）
