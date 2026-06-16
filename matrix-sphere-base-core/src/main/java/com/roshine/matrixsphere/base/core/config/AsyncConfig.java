package com.roshine.matrixsphere.base.core.config;

import com.roshine.matrixsphere.base.client.login.LoginInfo;
import com.roshine.matrixsphere.base.client.login.LoginInfoHelper;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author roshine
 * @version 2.0.0
 * 全局异步线程池配置 (解决跨线程上下文丢失问题)
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("matrixAsyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("matrix-async-");
        // 拒绝策略：由调用线程处理该任务（防丢）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 【核心魔法】设置任务装饰器，用于跨线程传递上下文
        executor.setTaskDecorator(new ContextCopyingDecorator());

        executor.initialize();
        return executor;
    }

    /**
     * 上下文拷贝装饰器 (将主线程的 ThreadLocal 拷贝到子线程)
     */
    static class ContextCopyingDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            // 1. 在主线程中获取上下文
            Map<String, String> contextMap = MDC.getCopyOfContextMap(); // 日志 MDC
            LoginInfo loginInfo = LoginInfoHelper.getUserInfo();        // 用户信息

            return () -> {
                try {
                    // 2. 在子线程中恢复上下文
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap);
                    }
                    if (loginInfo != null) {
                        LoginInfoHelper.setUserInfo(loginInfo);
                    }
                    // 3. 执行真正的异步业务逻辑
                    runnable.run();
                } finally {
                    // 4. 极其重要：执行完毕后清理子线程上下文，防止线程池复用导致的内存泄漏或数据串行
                    MDC.clear();
                    LoginInfoHelper.removeUserInfo();
                }
            };
        }
    }
}