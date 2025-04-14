package com.dihuan.user.controller;

import com.dihuan.common.result.Result;
import com.dihuan.model.dto.email.VerificationCodeDto;
import com.dihuan.user.service.UserEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Tag(name = "用户邮箱服务")
public class UserEmailController {

    @Autowired
    private UserEmailService userEmailService;

    @Operation(summary = "发送重置密码邮件")
    @GetMapping("sendResetPasswordEmail")
    public Result<String> sendResetPasswordEmail(@RequestParam String email) throws Exception {
        String verificationKey = userEmailService.sendResetPasswordEmail(email);
        return Result.success(verificationKey);
    }

    @Operation(summary = "发送注册账号验证邮箱邮件")
    @GetMapping("sendRegisterEmail")
    public Result sendRegisterEmail(@RequestParam String email) throws Exception {
        String verificationKey = userEmailService.sendRegisterEmail(email);
        return Result.success(verificationKey);
    }

}
