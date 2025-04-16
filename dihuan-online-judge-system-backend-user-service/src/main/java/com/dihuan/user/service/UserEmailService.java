package com.dihuan.user.service;

import com.dihuan.common.utils.emailVerificationUtils.EmailVerificationCodeVo;
import com.dihuan.model.dto.email.VerificationCodeDto;
import com.dihuan.model.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface UserEmailService {
    String sendResetPasswordEmail(String email) throws Exception;

    @Async("mailExecutor")
    void AsyncSendResetPasswordEmail(String email, User user, EmailVerificationCodeVo emailVerificationCodeObject) throws Exception;

    String sendRegisterEmail(String email) throws Exception;

    @Async("mailExecutor")
    void AsyncSendRegisterEmail(String email, EmailVerificationCodeVo emailVerificationCodeObject) throws Exception;
}
