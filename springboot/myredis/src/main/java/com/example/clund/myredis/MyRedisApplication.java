package com.example.clund.myredis;

import com.example.clund.myredis.lock.generator.CacheKeyGenerator;
import com.example.clund.myredis.lock.generator.LockKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * @author dashuai
 */
@SpringBootApplication
@EntityScan(basePackages = "com.example.clund.myredis")
public class MyRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyRedisApplication.class, args);
    }

    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return new LockKeyGenerator();
    }
}
