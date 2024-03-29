package com.lcc.tool.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁封装方法
 *
 * @author Liu Chunchi
 * @date 2023/11/17 14:22
 */
@Component
@Slf4j
public class LockService {

    @Resource
    private RedissonClient redissonClient;

    public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            throw new RuntimeException("请求太频繁了，请稍后再试哦~~");
        }
        try {
            return supplier.get();//执行锁内的代码逻辑
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    public void executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierWithNoReturn supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            throw new RuntimeException("请求太频繁了，请稍后再试哦~~");
        }
        try {
            supplier.apply();//执行锁内的代码逻辑
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier) {
        return executeWithLockThrows(key, waitTime, unit, supplier::get);
    }

    @SneakyThrows
    public void executeWithLock(String key, int waitTime, TimeUnit unit, SupplierWithNoReturn supplier) {
        executeWithLockThrows(key, waitTime, unit, supplier);
    }

    public <T> T executeWithLock(String key, Supplier<T> supplier) {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
    }


    @FunctionalInterface
    public interface SupplierWithNoReturn {
        void apply();
    }


    @FunctionalInterface
    public interface SupplierThrow<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }
}