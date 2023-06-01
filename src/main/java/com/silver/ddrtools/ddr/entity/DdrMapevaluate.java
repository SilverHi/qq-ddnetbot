package com.silver.ddrtools.ddr.entity;

import lombok.Data;

@Data
public class DdrMapevaluate {
    private Long id;

    private String mapName;

    private Integer star;

    private String content;

    private String userName;

    private String recommendPoints;

    private String userPoints;

    private String time;

    private Integer zan;
}