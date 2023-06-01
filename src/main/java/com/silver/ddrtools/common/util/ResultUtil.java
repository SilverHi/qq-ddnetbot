package com.silver.ddrtools.common.util;

import com.silver.ddrtools.common.enums.*;
import com.silver.ddrtools.common.entity.ResultBody;

/**
 * @ClassName ResultUtill
 * @Description TODO
 * @Author silver
 * @Date 2022/7/16 17:12
 * @Version 1.0
 **/

public class ResultUtil {
    public static ResultBody success(Object data){
        return new ResultBody(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }
    public static ResultBody fail(ResultCode resultCode){
        return new ResultBody(resultCode.getCode(), resultCode.getMsg(), null);
    }
    public static ResultBody fail(int code, String msg){
        return new ResultBody(code, msg, null);
    }
}
