package com.example.clund.myredis.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 锁的注解
 *
 * @author dashuai
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * @return redis 锁key的前缀
     */
    String prefix() default "";

    /**
     * 过期秒数，默认为5秒
     *
     * @return 轮询锁的时间
     */
    int expire() default 5;


    /**
     * 时间单位
     *
     * @return 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;


    /**
     * key 的分隔符
     *
     * @return String
     */
    String delimiter() default ":";
}
