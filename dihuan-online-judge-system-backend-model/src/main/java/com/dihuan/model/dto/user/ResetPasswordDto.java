package com.dihuan.model.dto.user;

import lombok.Data;

@Data
public class ResetPasswordDto {

    private String email;
    private String uuid;
    private String code;
    private String password;
}
