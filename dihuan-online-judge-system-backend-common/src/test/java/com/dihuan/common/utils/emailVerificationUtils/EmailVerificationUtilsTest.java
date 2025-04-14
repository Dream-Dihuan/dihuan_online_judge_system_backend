package com.dihuan.common.utils.emailVerificationUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailVerificationUtilsTest {

    @Test
    void generateVerificationCode() {
        String code = EmailVerificationUtils.generateVerificationCode();
        System.out.println(code);
    }
}