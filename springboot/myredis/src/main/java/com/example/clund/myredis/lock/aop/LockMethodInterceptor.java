package com.example.clund.myredis.lock.aop;


import com.example.clund.myredis.lock.annotation.CacheLock;
import com.example.clund.myredis.lock.generator.CacheKeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Method;

/**
 * 拦截器
 *
 * @author dashuai
 */
@Aspect
@Configuration
public class LockMethodInterceptor {

    private final StringRedisTemplate redisTemplate;
    private final CacheKeyGenerator keyGenerator;

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate redisTemplate, CacheKeyGenerator keyGenerator) {
        this.redisTemplate = redisTemplate;
        this.keyGenerator = keyGenerator;

    }

    @Around("execution(public * * (..)) && @annotation(com.example.clund.myredis.lock.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key can't be null...");
        }
        final String lockKey = keyGenerator.getLockKey(pjp);

        try {
            final Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, "");
            if (success) {
                System.out.println(lockKey);
                redisTemplate.expire(lockKey, lock.expire(), lock.timeUnit());
            } else {
                throw new RuntimeException("请勿重复请求");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            //实际应删除key
//            redisTemplate.delete(lockKey);
        }
        return null;
    }

}
