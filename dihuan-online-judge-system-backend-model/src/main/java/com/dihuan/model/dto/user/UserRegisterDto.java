package com.dihuan.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户注册Dto")
public class UserRegisterDto {
    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "邮箱UUID")
    private String uuid;

    @Schema(name = "邮箱验证码")
    private String code;
}
