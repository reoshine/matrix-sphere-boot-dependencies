package com.roshine.matrixsphere.base.core.utils;

import com.roshine.matrixsphere.base.client.utils.CommonUtils;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-11-02 21:03
 * @Description
 */
@Component
public class RedisLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static final String LOCK_KEY_FORMAT = "lock:{}:{}";

    private static final String LUA_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    private static StringRedisTemplate  redisTemplate;

    private RedisLock() {}

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        RedisLock.redisTemplate = redisTemplate;
    }

    public static <T> String getLockKey(@Nonnull String business, @Nonnull T resource) {
        return CommonUtils.format(LOCK_KEY_FORMAT, business, resource);
    }

    /**
     * 尝试加锁(仅一次)
     * @param lockKey 锁key
     * @param expireSeconds 锁超时时间(秒)
     * @return 加锁成功的UUID
     */
    public static String tryLock(@Nonnull String lockKey, int expireSeconds) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        boolean result = tryLock(lockKey, uuid, expireSeconds);
        return result ? uuid : null;
    }

    /**
     * 尝试加锁(仅一次)
     * @param lockKey 锁key
     * @param lockValue 锁value
     * @param expireSeconds 锁超时时间(秒)
     * @return true or false
     */
    public static boolean tryLock(@Nonnull String lockKey, @Nonnull String lockValue, int expireSeconds) {
        try {
            RedisCallback<Boolean> callback = (connection) ->
                    connection.set(lockKey.getBytes(StandardCharsets.UTF_8),
                            lockValue.getBytes(StandardCharsets.UTF_8),
                            Expiration.seconds(expireSeconds),
                            RedisStringCommands.SetOption.SET_IF_ABSENT);
            return Boolean.TRUE.equals(redisTemplate.execute(callback));
        }catch (Exception e) {
            logger.error("tryLock error: ", e);
        }
        return false;
    }

    /**
     * 加锁(指定最大尝试次数)
     * @param lockKey 锁key
     * @param expireSeconds 锁超时时间(秒)
     * @param tryTimes 最大尝试次数
     * @param sleepMillis 每两次间的休眠时间(毫秒)
     * @return 加锁成功的UUID
     */
    public static String lock(@Nonnull String lockKey, int expireSeconds, int tryTimes, long sleepMillis) {
        String uuid = CommonUtils.getUuid();
        boolean result = lock(lockKey, uuid, expireSeconds, tryTimes, sleepMillis);
        return result ? uuid : null;
    }

    /**
     * 加锁(指定最大尝试次数)
     * @param lockKey 锁key
     * @param lockValue 锁value
     * @param expireSeconds 锁超时时间(秒)
     * @param tryTimes 最大尝试次数
     * @param sleepMillis 每两次间的休眠时间(毫秒)
     * @return true or false
     */
    public static boolean lock(@Nonnull String lockKey, @Nonnull String lockValue, int expireSeconds, int tryTimes, long sleepMillis) {
        boolean result = false;
        int count = 0;
        try {
            do {
                count++;
                result = tryLock(lockKey, lockValue, expireSeconds);
                if (!result) {
                    TimeUnit.MILLISECONDS.sleep(sleepMillis);
                }
            }while (!result && count <= tryTimes);
        }catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解锁
     * @param lockKey 锁key
     * @param lockValue 锁value
     */
    public static void unlock(@Nonnull String lockKey, @Nonnull String lockValue) {
        try {
            RedisCallback<Boolean> callback = (connection) ->
                    connection.eval(LUA_SCRIPT.getBytes(), ReturnType.BOOLEAN ,
                            1,
                            lockKey.getBytes(StandardCharsets.UTF_8),
                            lockValue.getBytes(StandardCharsets.UTF_8));
            redisTemplate.execute(callback);
        }catch (Exception e) {
            logger.error("unlock error: ", e);
        }
    }
}
