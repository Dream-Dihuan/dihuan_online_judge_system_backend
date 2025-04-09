package com.dihuan.user.controller.inner;

import com.dihuan.serviceClient.service.UserFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
public class InnerUserController implements UserFeignClient {

    @GetMapping("feignPingUser")
    public String ping(){
        return "user_pong";
    }
}
