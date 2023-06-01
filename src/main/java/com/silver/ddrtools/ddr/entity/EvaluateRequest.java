package com.silver.ddrtools.ddr.entity;

import java.util.List;

import lombok.Data;

/**
 * @ClassName EvaluateRequest
 * @Description TODO
 * @Author silver
 * @Date 2022/12/4 10:47
 * @Version 1.0
 **/
@Data
public class EvaluateRequest {
    private String mapname;
    private String fastEvaluate;
    private String starpoint;
    private String textarea;
    private List<Integer> skilList;
    private String recommendpoints;
    private String playername;
    private String playerpoints;

}
