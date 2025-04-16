package com.dihuan.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserInfoDto {

    @Schema(description = "用户昵称")
    private String name;

    @Schema(description = "性别 (0-男/1-女)")
    private Integer gender;

    @Schema(description = "用户签名")
    private String description;

    @Schema(description = "生日")
    private Date birthday;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;
}
