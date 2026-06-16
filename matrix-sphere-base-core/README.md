# Matrix Sphere Base Core

核心支撑层 — 提供微服务通用的 Spring 基础设施，包括全局异常拦截、Redis 操作封装、MyBatis-Plus 自动填充与插件配置、JSON 序列化定制、异步线程池上下文透传等能力。

**依赖 `matrix-sphere-base-core-client` 模块**。

## 功能特性

- **全局异常处理**：`@RestControllerAdvice` 统一拦截各类异常，返回标准 `Response<T>` 格式
- **MyBatis-Plus 增强**：自动填充审计字段（创建人/时间、修改人/时间、逻辑删除、乐观锁版本号）、分页拦截器、乐观锁插件
- **Redis 全操作封装**：`RedisUtils` 覆盖 String、Hash、List、Set、ZSet 所有数据结构，整洁的静态方法调用
- **Jackson 全局配置**：时间格式化（`GMT+8` 时区，`yyyy-MM-dd HH:mm:ss`）、`BigDecimal` 转字符串保留两位小数、`Instant` 序列化
- **异步线程池**：父子线程间 MDC 日志上下文与 `LoginInfo` 登录上下文的自动透传
- **Spring 上下文持有者**：提供 `SpringContextHolder` 在非 Spring 管理类中获取 Bean 和环境属性
- **全局 CORS 配置**：解决微服务前后端分离的跨域问题

## 包结构

```
com.roshine.matrixsphere.base.core
├── config            # 全局配置类
│   ├── AsyncConfig                 # 异步线程池（上下文透传）
│   ├── BigDecimalStringSerializer # BigDecimal → 字符串序列化器
│   ├── JsonConfig                 # Jackson ObjectMapper 定制
│   ├── MybatisPlusConfig          # MyBatis-Plus 分页 + 乐观锁
│   └── RedisConfig                # RedisTemplate 序列化配置
├── handler           # 全局处理器
│   ├── GlobalExceptionHandler     # @RestControllerAdvice 异常拦截
│   └── MpMetaObjectHandler        # MyBatis-Plus 审计字段自动填充
└── utils             # 工具类
    ├── RedisUtils                 # Redis 全操作封装
    └── SpringContextHolder        # Spring 应用上下文持有者
```

## 配置详解

### 异步线程池（AsyncConfig）

- Bean 名称：`matrixAsyncExecutor`
- 核心线程数：`Runtime.getRuntime().availableProcessors()`（CPU 核心数）
- 最大线程数：核心线程数 × 2
- 队列容量：500
- 拒绝策略：`CallerRunsPolicy`（由调用线程执行）
- **上下文透传**：`ContextCopyingDecorator` 自动将主线程的 MDC 和 `LoginInfo` 复制到子线程，执行后清理

### JSON 序列化（JsonConfig）

- 时区：`GMT+8`（上海时间）
- 日期格式：`yyyy-MM-dd HH:mm:ss`
- `BigDecimal`：序列化为保留两位小数的字符串（HALF_UP 四舍五入），如 `"3.14"`
- `Instant`：转换为上海时区后格式化为 `yyyy-MM-dd HH:mm:ss`

### Redis 序列化（RedisConfig）

- Key：`StringRedisSerializer`
- Value：`Jackson2JsonRedisSerializer`（启用默认类型推断，支持泛型反序列化）

### MyBatis-Plus 自动填充（MpMetaObjectHandler）

| 字段          | 插入时                | 更新时       |
|-------------|--------------------|-----------|
| `createBy`  | 当前登录用户 ID / system | -         |
| `gmtCreate` | 当前时间               | -         |
| `modifyBy`  | 当前登录用户 ID / system | 当前登录用户 ID |
| `gmtModify` | 当前时间               | 当前时间      |
| `isDeleted` | 0                  | -         |
| `version`   | 0L                 | 由乐观锁管理    |

### 全局异常处理（GlobalExceptionHandler）

| 异常类型                                                 | HTTP 状态码 | 说明              |
|------------------------------------------------------|----------|-----------------|
| `MethodArgumentNotValidException`                    | 400      | 参数校验失败，汇总所有字段错误 |
| `BaseException` 及其子类                                 | 按异常指定    | 业务异常，保持原有 code  |
| `IllegalArgumentException` / `IllegalStateException` | 400      | 参数/状态不合法        |
| `Exception`（兜底）                                      | 500      | 未知异常，打印完整堆栈     |

## 使用示例

### Redis 操作

```java
@Autowired
private RedisUtils redisUtils;  // 或直接静态调用

// String
redisUtils.set("key", "value");
Object val = redisUtils.get("key");

// Hash
redisUtils.hPut("map", "field", "value");
Object v = redisUtils.hGet("map", "field");

// List
redisUtils.lLeftPush("list", item);
Object item = redisUtils.lRightPop("list");

// Set
redisUtils.sAdd("set", "member");
boolean member = redisUtils.sIsMember("set", "member");

// ZSet
redisUtils.zAdd("zset", "member", 100d);
Set<String> range = redisUtils.zRange("zset", 0, -1);
```

### Spring 上下文获取

```java
// 在非 Spring 管理的类中获取 Bean
UserService userService = SpringContextHolder.getBean(UserService.class);

// 获取环境变量
String profile = SpringContextHolder.getEnvProperty("spring.profiles.active");
```

### MyBatis-Plus 分页

```java
Page<User> page = new Page<>(pageNum, pageSize);
IPage<User> result = userMapper.selectPage(page, queryWrapper);
```

## 依赖

- `matrix-sphere-base-core-client`（内部模块）
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Data Redis
- Redisson Spring Boot Starter
- MyBatis-Plus Boot Starter
- Spring Boot Starter AOP
- Hutool All
- Lombok（provided）
