package com.dihuan.user.controller;


import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.Result;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.captchaUtils.CaptchaTypeEnum;
import com.dihuan.common.utils.captchaUtils.CaptchaUtils;
import com.dihuan.common.utils.captchaUtils.CaptchaVo;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.dto.user.*;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.model.vo.user.UserListItemVo;
import com.dihuan.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/")
@Tag(name = "用户服务")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "获取验证码")
    @GetMapping("getUserLoginCaptcha")
    public Result<CaptchaVo> getUserLoginCaptcha(){
        return CaptchaUtils.createCaptcha(CaptchaTypeEnum.LOGIN, stringRedisTemplate);
    }

    @Operation(summary = "用户登录")
    @PostMapping("userLogin")
    public Result<String> userLogin(@RequestBody UserLoginDto userLoginDto){
       String token = userService.userLogin(userLoginDto, stringRedisTemplate);
       return Result.success(token);
    }

    @Operation(summary = "用户注册")
    @PostMapping("userRegister")
    public Result userRegister(@RequestBody UserRegisterDto userRegisterDto){
        userService.userRegister(userRegisterDto);
        return Result.success();
    }

    @Operation(summary = "修改用户密码")
    @PostMapping("resetPassword")
    public Result resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
        userService.resetPassword(resetPasswordDto);
        return Result.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("getUserInfo")
    public Result<UserInfoVo> getUserInfo(@RequestParam(required = false)Long id){
        UserInfoVo userInfoVo = userService.getUserInfo(id);
        return Result.success(userInfoVo);
    }
    
    @Operation(summary = "修改用户信息")
    @PostMapping("updateUserInfo")
    public Result updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto){
        userService.updateUserInfo(updateUserInfoDto);
        return Result.success();
    }

    @Operation(summary = "获取用户信息列表")
    @PostMapping("getUserList")
    public Result<DihuanPage<UserListItemVo>> getUserList(@RequestBody UserListDto userListDto){
        DihuanPage<UserListItemVo> userList = userService.getUserList(userListDto);
        return Result.success(userList);
    }
}
