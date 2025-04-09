package com.dihuan.model.enums.question;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.dihuan.model.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

public enum JudgeStatusEnum implements BaseEnum {
    WAITING(0,"等待判题"),
    JUDGING(1,"判题中"),
    FINISHED(2,"判题完成");


    @EnumValue
    @JsonValue
    private Integer code;

    private String name;

    JudgeStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
