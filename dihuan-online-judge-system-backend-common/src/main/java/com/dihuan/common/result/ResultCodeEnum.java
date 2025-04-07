package com.dihuan.common.result;

import lombok.Getter;

/**
 *  统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),

    // captcha部分
    CAPTCHA_CODE_NOT_FOUND(301,"未输入验证码"),
    CAPTCHA_CODE_EXPIRED(302,"验证码已过期"),
    CAPTCHA_CODE_ERROR(303,"验证码错误");


    // 状态码
    private final Integer code;

    // 返回消息
    private final String message;

    ResultCodeEnum(Integer code,String message){
        this.code=code;
        this.message = message;
    }
}
