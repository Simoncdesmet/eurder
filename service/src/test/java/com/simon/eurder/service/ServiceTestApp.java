package com.simon.eurder.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:servicetestapp.properties")
@EntityScan(basePackages = "com.simon.eurder")
@SpringBootApplication(scanBasePackages = "com.simon.eurder", exclude={SecurityAutoConfiguration.class})
public class ServiceTestApp {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTestApp.class, args);
    }
}
