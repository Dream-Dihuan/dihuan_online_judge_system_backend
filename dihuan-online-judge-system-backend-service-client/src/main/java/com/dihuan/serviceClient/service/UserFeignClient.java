package com.dihuan.serviceClient.service;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dihuan-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    @GetMapping("feignPingUser")
    String ping();


    @GetMapping("increaseExperience")
    @Operation(summary = "增加用户经验值")
    void increaseExperience(@RequestParam("userId") Long userId, @RequestParam("amount") Long amount);

    @GetMapping("decreaseExperience")
    @Operation(summary = "减少用户经验值")
    void decreaseExperience(@RequestParam("userId") Long userId,@RequestParam("amount") Long amount);
}
