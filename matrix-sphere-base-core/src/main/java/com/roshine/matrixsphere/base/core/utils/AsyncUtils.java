package com.roshine.matrixsphere.base.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2021-05-03 21:02
 * @description 异步操作用具类
 */
@Slf4j
public class AsyncUtils {
    private static ExecutorService executor;

    static {
        executor = new ThreadPoolExecutor(10, 100, 60L * 5, TimeUnit.SECONDS, new SynchronousQueue<>());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executor.shutdown()));
    }

    /**
     * 并发执行任务，未设置超时时间(存在隐式异常情况，需要调用方处理)
     *
     * @param suppliers
     * @param <T>
     * @return
     */
    public static <T> List<T> executeThrown(Supplier<T>... suppliers) {
        if (suppliers == null || suppliers.length == 0) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (CompletableFuture<T> future : getCompletableFuture(suppliers)) {
            result.add(future.join());
        }
        return result;
    }

    public static <T> List<T> execute(Supplier<T>... suppliers) {
        return execute(-1, null, suppliers);
    }

    /**
     * 并发执行任务，出现异常则返回null
     * @param timeout
     * @param unit
     * @param suppliers
     * @param <T>
     * @return
     */
    public static <T> List<T> execute(long timeout, TimeUnit unit, Supplier<T>... suppliers) {
        if (suppliers == null || suppliers.length == 0) {
            return Collections.emptyList();
        }
        int activeThreadCount = ((ThreadPoolExecutor)executor).getActiveCount();
        if (activeThreadCount > 10) {
            log.debug("AsyncUtils active thread count: {}", activeThreadCount);
        }
        List<T> result = new ArrayList<>();
        for (CompletableFuture<T> future : getCompletableFuture(suppliers)) {
            T t = null;
            try {
                t = timeout > 0 ? future.get(timeout, unit) : future.get();
            } catch (Exception e) {
                log.error("future.get exception: {}", e);
            }
            result.add(t);
        }
        return result;
    }

    private static <T> List<CompletableFuture<T>> getCompletableFuture(Supplier<T>[] suppliers) {
        List<CompletableFuture<T>> list = new ArrayList<>();
        for (Supplier<T> supplier : suppliers) {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, executor);
            list.add(future);
        }
        return list;
    }

    public static void executeRunnable(Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            return;
        }
        for (Runnable task : tasks) {
            CompletableFuture.runAsync(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("task.run exception: {}", e);
                }
            }, executor);
        }
    }
}
