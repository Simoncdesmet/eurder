package com.simon.eurder.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:domaintestapp.properties")
@SpringBootApplication(scanBasePackages = "com.simon.eurder", exclude={SecurityAutoConfiguration.class})
public class DomainTestApp {

    public static void main(String[] args) {
        SpringApplication.run(DomainTestApp.class, args);
    }
}
