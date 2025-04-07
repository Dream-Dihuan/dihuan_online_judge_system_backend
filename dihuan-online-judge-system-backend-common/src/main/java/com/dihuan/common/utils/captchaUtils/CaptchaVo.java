package com.dihuan.common.utils.captchaUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "验证码数据返回对象")
public class CaptchaVo {
    @Schema(name = "图片")
    private String image;

    @Schema(name = "验证码key")
    private String key;
}
