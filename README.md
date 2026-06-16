# Matrix Sphere Boot Dependencies

Matrix Sphere 企业级微服务底层基础架构 — 统一依赖管理与核心模块聚合。

## 项目简介

本工程是 Matrix Sphere 微服务体系的 **根项目（Parent POM）**，用于统一管理所有子模块的依赖版本、插件配置与仓库地址。子模块涵盖通用客户端库、核心支撑层以及安全认证组件，为上层业务服务提供开箱即用的基础能力。

通过此项目，各业务服务无需自行维护版本号，只需引入所需模块即可获得一致的运行环境。

## 模块概览

| 模块                               | 说明                                          | 依赖关系                  |
|----------------------------------|---------------------------------------------|-----------------------|
| `matrix-sphere-base-core-client` | 通用客户端库（DTO、异常、请求/响应、工具类）                    | 无 Spring 容器依赖，纯 Java  |
| `matrix-sphere-base-core`        | 核心支撑层（全局异常处理、Redis、MyBatis-Plus、JSON 配置）    | 依赖 `base-core-client` |
| `matrix-sphere-auth-core`        | 安全认证核心（OAuth2 Resource Server、CORS、SSO 上下文） | 依赖 `base-core`        |

## 技术栈

| 类别     | 技术                            | 版本             |
|--------|-------------------------------|----------------|
| 基础框架   | Spring Boot                   | 3.1.5          |
| 微服务    | Spring Cloud                  | 2022.0.4       |
| 服务治理   | Spring Cloud Alibaba          | 2022.0.0.0-RC2 |
| ORM    | MyBatis-Plus                  | 3.5.4.1        |
| 数据库连接池 | Druid                         | 1.2.19         |
| 缓存     | Redisson                      | 3.24.3         |
| RPC    | Apache Dubbo                  | 3.2.5          |
| 消息队列   | RocketMQ                      | 2.2.3          |
| API 文档 | Knife4j                       | 4.5.0          |
| 工具库    | Hutool、Guava、MapStruct、Lombok | -              |
| 国密加密   | sm-crypto、Bouncy Castle       | -              |

## 环境要求

- JDK 17+
- Apache Maven 3.8+
- Nexus 私服（用于依赖管理与发布）

## 快速开始

```bash
# 克隆项目
git clone http://192.168.0.91:8088/matrixsphere/matrix-sphere-boot-dependencies.git

# 编译安装到本地仓库（跳过测试）
mvn clean install -Dmaven.test.skip=true

# 部署到远程私服
mvn deploy
```

## 引入方式

在业务服务的 `pom.xml` 中引入所需模块：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.roshine.matrixsphere</groupId>
            <artifactId>matrix-sphere-boot-dependencies</artifactId>
            <version>${matrix-sphere.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>com.roshine.matrixsphere</groupId>
        <artifactId>matrix-sphere-base-core</artifactId>
    </dependency>
    <!-- 按需引入其他模块 -->
</dependencies>
```

## CI/CD

项目集成 Jenkins Pipeline，自动执行以下流程：

1. 从 GitLab 拉取指定分支代码
2. 执行 `mvn clean package`（跳过测试）
3. 执行 `mvn deploy` 发布到 Nexus 私服
4. 清理工作空间

## 发布管理

- **Releases**: `http://192.168.0.90:8089/repository/maven-releases/`
- **Snapshots**: `http://192.168.0.90:8089/repository/maven-snapshots/`

## 许可证

本项目基于 Apache License 2.0 许可协议。
