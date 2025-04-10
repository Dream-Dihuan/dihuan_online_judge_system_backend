package com.dihuan.model.enums.question;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.dihuan.model.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QuestionResultEnum implements BaseEnum {
    DEFAULT(0,"未尝试"),
    PASSED(1,"通过"),
    FAILED(2,"未通过");


    @EnumValue
    @JsonValue
    private Integer code;

    private String name;

    QuestionResultEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
