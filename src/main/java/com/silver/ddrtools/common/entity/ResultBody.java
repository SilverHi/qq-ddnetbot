package com.silver.ddrtools.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

/**
 * @ClassName ResultBody
 * @Description TODO
 * @Author silver
 * @Date 2022/7/16 17:10
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ResultBody {
    private int code;
    private String msg;
    private Object data;

}
