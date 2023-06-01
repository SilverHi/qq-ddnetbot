package com.silver.ddrtools.ddr.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LastFinishesBean
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 23:03
 * @Version 1.0
 **/

@NoArgsConstructor
@Data
public class LastFinishesBean {
    /**
     * timestamp : 1668862029
     * map : Koule 6
     * time : 1090.12
     * country : CHN
     * type : Oldschool
     */

    private String timestamp;
    private String map;
    private String time;
    private String country;
    private String type;
}
