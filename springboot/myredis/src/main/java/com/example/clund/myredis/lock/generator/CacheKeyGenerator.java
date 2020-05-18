package com.example.clund.myredis.lock.generator;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * key 生成器
 *
 * @author dashuai
 */
public interface CacheKeyGenerator {

    /**
     * 获取AOP参数 生成指定缓存key
     *
     * @param pjp
     * @return key
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
