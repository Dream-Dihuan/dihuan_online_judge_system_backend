package com.dihuan.user.controller;

import com.dihuan.common.result.Result;
import com.dihuan.user.service.UserSubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "用户关注服务")
public class UserSubscribeController {

    @Autowired
    private UserSubscribeService userSubscribeService;


    @GetMapping("subscribe")
    @Operation(summary = "关注用户")
    public Result subscribe(@RequestParam Long targetUserId) {
        userSubscribeService.subscribe(targetUserId);
        return Result.success();
    }

    @GetMapping("unSubscribe")
    @Operation(summary = "取消关注用户")
    public Result unSubscribe(@RequestParam Long targetUserId) {
        userSubscribeService.unSubscribe(targetUserId);
        return Result.success();
    }


}
