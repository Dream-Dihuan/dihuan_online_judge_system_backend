package com.dihuan.question;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@MapperScan("com.dihuan.userService.mapper")
@ComponentScan("com.dihuan")
@EnableFeignClients(basePackages = {"com.dihuan.serviceClient.service"})
public class QuestionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestionServiceApplication.class, args);
    }
}
