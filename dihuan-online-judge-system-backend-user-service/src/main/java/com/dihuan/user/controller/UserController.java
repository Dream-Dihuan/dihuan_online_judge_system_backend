package com.dihuan.user.controller;


import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.Result;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.dto.user.UpdateUserInfoDto;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/")
@Tag(name = "用户服务")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam(value = "userId")long id){
        User user = userService.getUser(id);
        return user;
    }

    @GetMapping("testException")
    public Result testException(@RequestParam String message){
        if(!Objects.equals(message, "pass")){
            throw new DihuanException(ResultCodeEnum.FAIL,"迪幻类型错误");
        }
        return Result.success();
    }

    @GetMapping("threadLocalGetTokenInfo")
    public void TokenHolder(){
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();
        System.out.println(tokenInfo);
    }

    @GetMapping("testPage")
    public Result<DihuanPage<User>> testPage(){
        DihuanPage<User> userDihuanPage = new DihuanPage<>(1, 3);
        DihuanPage<User> result = userService.page(userDihuanPage);
        return Result.success(result);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("getUserInfo")
    public Result<UserInfoVo> getUserInfo(){
        UserInfoVo userInfoVo = userService.getUserInfo();
        return Result.success(userInfoVo);
    }
    
    @Operation(summary = "修改用户信息")
    @PostMapping("updateUserInfo")
    public Result updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto){
        userService.updateUserInfo(updateUserInfoDto);
        return Result.success();
    }
}
