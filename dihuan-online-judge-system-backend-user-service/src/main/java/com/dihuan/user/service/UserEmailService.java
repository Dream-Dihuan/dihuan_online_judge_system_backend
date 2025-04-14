package com.dihuan.user.service;

import com.dihuan.model.dto.email.VerificationCodeDto;
import jakarta.mail.MessagingException;

public interface UserEmailService {
    String sendResetPasswordEmail(String email) throws MessagingException, Exception;

    String sendRegisterEmail(String email) throws MessagingException, Exception;
}
