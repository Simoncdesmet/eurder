package com.simon.eurder.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.simon.eurder")
@EntityScan(basePackages = "com.simon.eurder")
@EnableJpaRepositories(basePackages = "com.simon.eurder.repository")
public class EurderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurderApplication.class, args);
    }

}
