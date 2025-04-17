package com.dihuan.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.dihuan.serviceClient.service"})
public class AIServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AIServiceApplication.class, args);
    }

}
