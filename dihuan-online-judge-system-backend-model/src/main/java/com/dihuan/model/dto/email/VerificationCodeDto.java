package com.dihuan.model.dto.email;

import lombok.Data;

@Data
public class VerificationCodeDto {
    private String uuid;
    private String email;
    private String code;
}
