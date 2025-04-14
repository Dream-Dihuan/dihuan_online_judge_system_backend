package com.dihuan.common.utils.emailVerificationUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCodeVo {
    private String uuid;
    private String code;
}
