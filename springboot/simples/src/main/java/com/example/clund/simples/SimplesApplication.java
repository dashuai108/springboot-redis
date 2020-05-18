package com.example.clund.simples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author dashuai
 */
@EnableEurekaClient
@SpringBootApplication
public class SimplesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimplesApplication.class, args);
    }
}
