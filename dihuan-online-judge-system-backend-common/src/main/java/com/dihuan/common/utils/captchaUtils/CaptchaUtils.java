package com.dihuan.common.utils.captchaUtils;



import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.Result;
import com.dihuan.common.result.ResultCodeEnum;
import com.wf.captcha.SpecCaptcha;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

public class CaptchaUtils {
    public static Result<CaptchaVo> createCaptcha(CaptchaTypeEnum captchaTypeEnum, StringRedisTemplate stringRedisTemplate){
        String key;
        String code;

        SpecCaptcha specCaptcha = new SpecCaptcha(130,48,5);
        code = specCaptcha.text().toLowerCase();
        key = captchaTypeEnum.getCaptchaPrefix()+ UUID.randomUUID();

        stringRedisTemplate.opsForValue().set(key,code, captchaTypeEnum.getTtl(), captchaTypeEnum.getTimeUnit());
        return Result.success(new CaptchaVo(specCaptcha.toBase64(),key));
    }

    public static boolean verifyCaptchaCode(String _key,String _code,StringRedisTemplate stringRedisTemplate){
        String code = stringRedisTemplate.opsForValue().get(_key);
        if(_code==null){
            throw new DihuanException(ResultCodeEnum.CAPTCHA_CODE_NOT_FOUND);
        }

        if(code == null){
            throw new DihuanException(ResultCodeEnum.CAPTCHA_CODE_EXPIRED);
        }

        if(!code.equals(_code.toLowerCase())){
            throw new DihuanException(ResultCodeEnum.CAPTCHA_CODE_ERROR);
        }

        return true;
    }

}

