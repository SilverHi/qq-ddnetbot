package com.silver.ddrtools.ddr.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ActivityBean
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 23:05
 * @Version 1.0
 **/

@NoArgsConstructor
@Data
public class ActivityBean {

    /**
     * date : 2022-09-30
     * hours_played : 2
     */

    private String date;
    private int hours_played;
}
