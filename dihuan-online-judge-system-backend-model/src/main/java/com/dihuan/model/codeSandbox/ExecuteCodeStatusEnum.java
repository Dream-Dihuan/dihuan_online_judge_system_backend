package com.dihuan.model.codeSandbox;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.dihuan.model.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExecuteCodeStatusEnum implements BaseEnum {

    SUCCESS(0,"成功执行"),
    CODE_COMPILE_ERROR(1,"编译错误"),

    CODE_EXECUTION_ERROR(2,"代码执行错误"),
    DOCKER_PULL_IMAGE_ERROR(3,"拉取镜像错误"),
    DOCKER_RUN_CONTAINER_ERROR(4,"运行容器错误");


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
