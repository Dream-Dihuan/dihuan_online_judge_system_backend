package com.dihuan.common.utils.emailVerificationUtils;

import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.UUID;

public class EmailVerificationUtils {

    public static String generateVerificationCode() {
        // 包含所有数字和大写字母（不排除易混淆字符）
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            verificationCode.append(characters.charAt(index));
        }

        return verificationCode.toString();
    }

    public static EmailVerificationCodeVo createEmailVerificationCode(EmailConstantEnum emailConstantEnum,String email, StringRedisTemplate stringRedisTemplate){
        String uuid = UUID.randomUUID().toString();
        String code_key = emailConstantEnum.getPREFIX()+ uuid + emailConstantEnum.getCODE_SUFFFIX() ;
        String email_key = emailConstantEnum.getPREFIX()+ uuid + emailConstantEnum.getEMAIL_SUFFFIX() ;
        String code = generateVerificationCode();

        stringRedisTemplate.opsForValue().set(code_key,code, emailConstantEnum.getTTL(), emailConstantEnum.getTIME_UNIT());
        stringRedisTemplate.opsForValue().set(email_key,email, emailConstantEnum.getTTL(), emailConstantEnum.getTIME_UNIT());

        EmailVerificationCodeVo emailVerificationCodeVo = new EmailVerificationCodeVo(uuid, code);
        return emailVerificationCodeVo;
    }

    
    public static Boolean verifyEmailVerificationCode(EmailConstantEnum emailConstantEnum,String uuid, String code,String email, StringRedisTemplate stringRedisTemplate){
        String code_key = emailConstantEnum.getPREFIX()+ uuid + emailConstantEnum.getCODE_SUFFFIX() ;
        String email_key = emailConstantEnum.getPREFIX()+ uuid + emailConstantEnum.getEMAIL_SUFFFIX() ;
        String code_value = stringRedisTemplate.opsForValue().get(code_key);
        String email_value = stringRedisTemplate.opsForValue().get(email_key);

        if(StringUtils.isBlank(code)){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_NOT_FOUND);
        }

        if(code_value == null){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_EXPIRED);
        }

        if (!email_value.equals(email)){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_EMAIL_ADDRESS_ERROR);
        }

        if(!code.toLowerCase().equals(code_value.toLowerCase())){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_ERROR);
        }

        return true;
    }
}
