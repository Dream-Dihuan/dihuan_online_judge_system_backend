package com.dihuan.common.utils.emailVerificationUtils;

import java.util.concurrent.TimeUnit;

public enum EmailConstantEnum {

    RESET_PASSWORD("email:resetPassword:",15L,TimeUnit.MINUTES,":code",":email"),
    REGISTER_ACCOUNT("email:register:",15L,TimeUnit.MINUTES,":code",":email");

    public String getPREFIX() {
        return PREFIX;
    }

    public Long getTTL() {
        return TTL;
    }

    public TimeUnit getTIME_UNIT() {
        return TIME_UNIT;
    }

    public String getCODE_SUFFFIX() {
        return CODE_SUFFFIX;
    }

    public String getEMAIL_SUFFFIX() {
        return EMAIL_SUFFFIX;
    }

    private String PREFIX;

    private Long TTL;

    private TimeUnit TIME_UNIT;

    private String CODE_SUFFFIX;

    private String EMAIL_SUFFFIX;


    EmailConstantEnum(String PREFIX, Long TTL, TimeUnit TIME_UNIT, String CODE_SUFFFIX, String EMAIL_SUFFFIX) {
        this.PREFIX = PREFIX;
        this.TTL = TTL;
        this.TIME_UNIT = TIME_UNIT;
        this.CODE_SUFFFIX = CODE_SUFFFIX;
        this.EMAIL_SUFFFIX = EMAIL_SUFFFIX;
    }
}
