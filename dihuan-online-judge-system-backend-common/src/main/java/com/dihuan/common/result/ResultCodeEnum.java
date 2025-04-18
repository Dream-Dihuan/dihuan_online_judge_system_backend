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
    CAPTCHA_CODE_ERROR(303,"验证码错误"),

    EMAIL_VERIFICATION_CODE_NOT_FOUND(311,"未输入邮箱验证码"),
    EMAIL_VERIFICATION_CODE_EXPIRED(312,"邮箱验证码已过期"),
    EMAIL_VERIFICATION_CODE_ERROR(313,"邮箱验证码错误"),
    EMAIL_VERIFICATION_CODE_EMAIL_ADDRESS_ERROR(314,"邮件验证码所属关系异常"),
    EMAIL_VERIFICATION_CODE_USER_NOT_EXIST_ERROR(315,"该邮箱未绑定用户"),
    EMAIL_VERIFICATION_CODE_USER_EXIST_ERROR(316,"该邮箱已绑定用户"),

    USER_SUBSCRIBE_SELF_ERROR(401,"不能关注自己"),
    USER_SUBSCRIBE_SAME_ERROR(402,"已关注过该用户"),
    USER_SUBSCRIBE_ERROR(403,"关注失败"),
    USER_UNSUBSCRIBE_NOT_FOUND_ERROR(404,"未关注过该用户"),
    USER_UNSUBSCRIBE_ERROR(405,"取消关注失败"),

    USER_NOT_FOUND_ERROR(406,"用户不存在"),
    USER_PASSWORD_ERROR(407,"密码错误"),
    USER_SAME_USERNAME_ERROR(408,"用户名已存在"),
    USER_SAME_EMAIL_ERROR(409,"邮箱已存在"),
    USER_REGISTER_ERROR(410,"用户注册失败"),
    USER_RESET_PASSWORD_ERROR(411,"修改密码失败"),
    USER_BANNED_ERROR(412,"用户被封禁"),

    ADMINISTRATOR_PERMISSION_ERROR(501,"管理员权限不足"),

    QUESTION_NOT_FOUND_ERROR(601,"题目不存在");

    // 状态码
    private final Integer code;

    // 返回消息
    private final String message;

    ResultCodeEnum(Integer code,String message){
        this.code=code;
        this.message = message;
    }
}
