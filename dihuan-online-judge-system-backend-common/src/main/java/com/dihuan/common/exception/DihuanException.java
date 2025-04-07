package com.dihuan.common.exception;


import com.dihuan.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class DihuanException extends RuntimeException{
    private Integer code;

    public DihuanException(Integer code,String message){
        super(message);
        this.code=code;
    }

    public DihuanException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
    }

    public DihuanException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.code=resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "DihuanException{" +
                "code=" + code +",message:"+getMessage()+
                '}';
    }
}
