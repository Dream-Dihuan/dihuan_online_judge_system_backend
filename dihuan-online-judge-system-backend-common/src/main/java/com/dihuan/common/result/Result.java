package com.dihuan.common.result;

import lombok.Data;

@Data
public class Result<T> {

    // 返回码
    private Integer code;

    // 返回消息
    private String message;

    // 返回数据
    private T data;

    private static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> Result<T> success(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> success(){
        return build(null,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(){
        return build(null,ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }

    public static<T> Result<T> fail(Integer code, String message) {
        Result<T>result = build(null);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }
}
