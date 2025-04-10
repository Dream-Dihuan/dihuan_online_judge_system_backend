package com.dihuan.model.codeSandbox;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.dihuan.model.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExecuteCodeStatusEnum implements BaseEnum {

    SUCCESS(0,"成功执行"),
    CODE_ERROR(1,"用户代码执行错误"),
    SANDBOX_ERROR(2,"代码沙箱系统错误");


    @EnumValue
    @JsonValue
    private Integer code;

    private String name;

    ExecuteCodeStatusEnum(Integer code, String name){
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
