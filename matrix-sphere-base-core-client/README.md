# Matrix Sphere Base Core Client

通用客户端库 — 提供微服务间共享的基础 DTO、异常定义、请求/响应封装、登录上下文及纯 Java 工具类。

**不依赖 Spring 容器**，可在任何 Java 环境中使用。

## 功能特性

- **统一响应格式**：`Response<T>` 封装 + `ResultCode` 状态码枚举
- **分页模型**：`PageRequest` 请求参数 + `PageResponse<T>` 响应体
- **异常体系**：统一的业务异常层次结构（参数异常、MQ 异常、RPC 异常、Token 过期等）
- **登录上下文**：`LoginInfo` / `LoginAccount` 领域模型 + `LoginInfoHelper` ThreadLocal 持有者
- **工具类**：国密 SM4 加密、钉钉机器人告警推送

## 包结构

```
com.roshine.matrixsphere.base.client
├── constant          # 通用常量定义（编码、状态码等）
├── dto               # 数据传输对象（PageResponse）
├── exception         # 异常层次结构
│   ├── ErrorCode              # 错误码接口
│   ├── BaseException          # 业务异常基类
│   ├── ArgumentException      # 参数校验异常（400）
│   ├── MqException            # 消息队列异常
│   ├── RPCException           # RPC 调用异常
│   └── TokenIsExpiredException# Token 过期异常（401）
├── login             # 登录上下文模型
│   ├── LoginAccount           # SSO 登录账户信息
│   ├── LoginInfo              # 登录上下文包装
│   └── LoginInfoHelper        # ThreadLocal 上下文持有者
├── req               # 请求模型（PageRequest）
├── response          # 响应模型
│   ├── Response               # 统一 API 响应体
│   └── ResultCode             # 全局状态码枚举
└── utils             # 工具类
    ├── DingDingUtils          # 钉钉机器人告警
    └── Sm4Utils               # 国密 SM4 加密
```

## 异常层次结构

```
RuntimeException
  └── BaseException (code + message)
        ├── ArgumentException       → 400 参数校验失败
        ├── MqException             → MQ 消息异常
        ├── RPCException            → 远程调用异常
        └── TokenIsExpiredException → 401 Token 过期
```

所有业务异常均实现 `ErrorCode` 接口，确保错误码与错误信息的统一规范。

## 使用示例

### 统一响应

```java
// 成功响应
return Response.success(data);

// 失败响应
return Response.fail("操作失败");
return Response.fail(500, "系统内部异常");
```

### 分页查询

```java
// 请求（参数校验由 Jakarta Validation 保证）
PageRequest pageReq = new PageRequest();
pageReq.setPageNum(1);
pageReq.setPageSize(20);

// 响应
PageResponse<UserDTO> pageResp = PageResponse.of(total, list);
```

### 异常抛出

```java
throw new ArgumentException("参数不能为空");
throw new TokenIsExpiredException();
throw new RPCException("调用用户服务失败", cause);
```

### ThreadLocal 登录上下文

```java
// 设置上下文
LoginInfoHelper.setUserInfo(loginInfo);

// 获取当前用户 ID
String userId = LoginInfoHelper.getUserId();

// 使用完毕后务必清除
LoginInfoHelper.removeUserInfo();
```

### SM4 加解密

```java
String cipherText = Sm4Utils.encryptHex("明文数据", "密钥");
String plainText  = Sm4Utils.decryptStr(cipherText, "密钥");
```

### 钉钉告警

```java
DingDingUtils.sendTextMessage(webhookUrl, "告警内容", true);
```

## 依赖

- Lombok（provided）
- Hutool All
- Jackson Annotations
- Jakarta Servlet API（provided）
- Jakarta Validation API
- SLF4J API
