package com.example.clund.myredis.fenbu.redislock;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis 实现分布式获取或
 *
 * @author dashuai
 */
@Component
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 尝试 获取锁
     *
     * @param key
     * @param value
     * @return boolean
     */
    public boolean tryLock(String key, String value) {

        //如果存入redis则获取锁
        if (redisTemplate.opsForValue().setIfAbsent(key, value, 100, TimeUnit.SECONDS)) {
            return true;
        }
        //如果key 存在说明已有对象获取锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间 如果高并发的情况可能会出现已经被修改的问题  所以多一次判断保证线程的安全
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param key
     * @param value
     */
    public void unLock(String key, String value) {

        String currentValue = (String) redisTemplate.opsForValue().get(key);

        try {
            if (StringUtils.isNotEmpty(currentValue) && currentValue.equals(value)) {
                Boolean delete = redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
