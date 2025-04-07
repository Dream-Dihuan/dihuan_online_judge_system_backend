package com.dihuan.serviceClient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient(name = "dihuan-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    @GetMapping("feignPingUser")
    String ping();

}
