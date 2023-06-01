package com.silver.ddrtools.common.Exception;

import com.silver.ddrtools.common.enums.ResultCode;

import lombok.Data;
import lombok.Getter;

/**
 * @ClassName BusinessException
 * @Description TODO
 * @Author silver
 * @Date 2022/7/16 18:22
 * @Version 1.0
 **/
@Data
public class BusinessException extends RuntimeException{
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description=description;
    }
    public BusinessException(ResultCode resultCode, String description) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.description=description;
    }
}
