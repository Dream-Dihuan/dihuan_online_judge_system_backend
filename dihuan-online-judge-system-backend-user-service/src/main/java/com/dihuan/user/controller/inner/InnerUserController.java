package com.dihuan.user.controller.inner;

import com.dihuan.serviceClient.service.UserFeignClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
@Tag(name = "内部-用户服务")
public class InnerUserController implements UserFeignClient {

    @GetMapping("feignPingUser")
    public String ping(){
        return "user_pong";
    }
}
