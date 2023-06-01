package com.silver.ddrtools.common.Exception;

import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.common.enums.ResultCode;
import com.silver.ddrtools.common.util.ResultUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author silver
 * @Date 2022/7/16 18:23
 * @Version 1.0
 **/
@RestControllerAdvice
@Component
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResultBody businessExceptionHandler(BusinessException e){
        e.printStackTrace();
        return ResultUtil.fail(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultBody runtimeExceptionHandler(RuntimeException e){
        e.printStackTrace();

        return ResultUtil.fail(ResultCode.SYSTEM_ERROR);
    }
}
