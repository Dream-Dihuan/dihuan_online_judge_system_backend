package com.dihuan.user.controller.inner;

import com.dihuan.serviceClient.service.UserFeignClient;
import com.dihuan.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
@Tag(name = "内部-用户服务")
public class InnerUserController implements UserFeignClient {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("feignPingUser")
    public String ping(){
        return "user_pong";
    }

    @GetMapping("increaseExperience")
    @Operation(summary = "增加用户经验值")
    public void increaseExperience(@RequestParam Long userId,@RequestParam Long amount){
        userMapper.increaseExperience(userId,amount);
    }

    @GetMapping("decreaseExperience")
    @Operation(summary = "减少用户经验值")
    public void decreaseExperience(@RequestParam Long userId,@RequestParam Long amount){
        userMapper.decreaseExperience(userId,amount);
    }
}
