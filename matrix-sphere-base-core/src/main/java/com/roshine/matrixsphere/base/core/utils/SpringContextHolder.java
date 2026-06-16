package com.roshine.matrixsphere.base.core.utils;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author roshine
 * @version 2.0.0
 * 企业级 Spring 容器上下文工具类
 * 核心特性：
 * 1. @Lazy(false) 强制提早初始化，防止其他 Bean 调用时报空指针。
 * 2. 实现 DisposableBean，在应用上下文销毁时清理静态变量，防止内存泄漏。
 * 3. 增加强校验拦截，杜绝上下文未加载完的非法调用。
 */
@Slf4j
@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /**
     * 容器启动时注入上下文
     */
    @Override
    public void setApplicationContext(@Nonnull ApplicationContext context) throws BeansException {
        applicationContext = context;
        log.info("🔧 SpringContextHolder 已成功接管 ApplicationContext");
    }

    /**
     * 容器销毁时清理静态引用，防止内存泄漏
     */
    @Override
    public void destroy() {
        log.info("🧹 SpringContextHolder 正在清理 ApplicationContext 引用");
        applicationContext = null;
    }

    /**
     * 内部断言：确保在使用前 applicationContext 已经被注入
     */
    private static void assertContextInjected() {
        Assert.state(applicationContext != null,
                "ApplicationContext 尚未被注入! 请确认 Spring 容器已正常启动且未发生循环依赖。");
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        assertContextInjected();
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        assertContextInjected();
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        assertContextInjected();
        return applicationContext.getType(name);
    }

    public static String[] getAliases(String name) {
        assertContextInjected();
        return applicationContext.getAliases(name);
    }

    /**
     * 获取环境配置参数 (如 application.yml 中的参数)
     */
    public static String getEnvProperty(@Nonnull String key) {
        assertContextInjected();
        return applicationContext.getEnvironment().getProperty(key);
    }
}