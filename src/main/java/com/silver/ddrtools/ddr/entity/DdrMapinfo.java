package com.silver.ddrtools.ddr.entity;

import lombok.Data;

@Data
public class DdrMapinfo {
    private Long id;

    private String mapname;

    private String website;

    private String thumbnail;

    private String webpreview;

    private String maptype;

    private Integer points;

    private Integer difficulty;

    private String mapper;

    private String maprelease;

    private Integer width;

    private Integer height;

    private Integer goodnum;

    private Integer badnum;
}