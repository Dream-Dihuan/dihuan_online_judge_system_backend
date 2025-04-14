package com.dihuan.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户登录Dto")
public class UserLoginDto {

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "验证码")
    private String captchaCode;

    @Schema(name = "验证码key")
    private String captchaKey;
}
