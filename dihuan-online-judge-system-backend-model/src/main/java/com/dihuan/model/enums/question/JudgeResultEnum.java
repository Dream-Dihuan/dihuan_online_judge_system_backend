package com.dihuan.model.enums.question;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.dihuan.model.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;


public enum JudgeResultEnum implements BaseEnum {
    ACCEPTED(0,"通过"),
    WRONG_ANSWER(1,"答案错误"),
    TIME_LIMIT_EXCEEDED(2,"超出时间限制"),
    MEMORY_LIMIT_EXCEEDED(3,"超出内存限制"),
    RUNTIME_ERROR(4,"运行时错误"),
    CODE_COMPILE_ERROR(5,"代码编译错误");

    @EnumValue
    @JsonValue
    private Integer code;

    private String name;

    JudgeResultEnum(Integer code, String name){
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
