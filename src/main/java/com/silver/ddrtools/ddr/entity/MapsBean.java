package com.silver.ddrtools.ddr.entity;

import lombok.Data;

/**
 * @ClassName MapsBean
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 23:44
 * @Version 1.0
 **/
@Data
public class MapsBean {
    private String map_name;
    private Integer points;
    private Integer total_finishes;
    private Integer finishes;
    private Integer rank;
    private Integer team_rank;
    private Double time;
    private Integer first_finish;
}
