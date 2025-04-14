package com.dihuan.common.utils.captchaUtils;

import java.util.concurrent.TimeUnit;

public enum CaptchaTypeEnum {

    LOGIN("app:login:",60,TimeUnit.SECONDS);

    private String captchaPrefix;

    private Integer ttl;

    private TimeUnit timeUnit;

    CaptchaTypeEnum(String captchaPrefix, Integer ttl, TimeUnit timeUnit){
        this.captchaPrefix=captchaPrefix;
        this.ttl=ttl;
        this.timeUnit=timeUnit;
    }

    public String getCaptchaPrefix() {
        return this.captchaPrefix;
    }

    public Integer getTtl() {
        return this.ttl;
    }

    public TimeUnit getTimeUnit(){
        return this.timeUnit;
    }
}
