package com.silver.ddrtools.common.entity;

import lombok.Data;

@Data
public class SysSetting {
    private Long id;

    private String type;

    private String name;

    private String value;

    private String bief;
}