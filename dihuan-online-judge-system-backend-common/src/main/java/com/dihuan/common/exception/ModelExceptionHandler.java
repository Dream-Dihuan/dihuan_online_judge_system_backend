package com.dihuan.common.exception;


import com.dihuan.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ModelExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(DihuanException.class)
    @ResponseBody
    public Result handle(DihuanException e){
        e.printStackTrace();
        return Result.fail(e.getCode(),e.getMessage());
    }

}
