package com.silver.ddrtools.ddr.entity;

import java.util.List;

import lombok.Data;

/**
 * @ClassName NoviceBean
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 22:55
 * @Version 1.0
 **/
@Data
public class MapInfoBean {

    /**
     * points : {"total":486,"points":467,"rank":4124}
     * team_rank : {"rank":"unranked"}
     * rank : {"rank":"unranked"}
     * maps : {"Give u the Moon":{"points":3,"total_finishes":25490,"finishes":1,"rank":7058,"time":406.34,"first_finish":1664970449},"Teleport":{"points":3,"total_finishes":22092,"finishes":1,"rank":15382,"time":1264.28,"first_finish":1666514755},"xyz_ddrace2":{"points":3,"total_finishes":31314,"finishes":1,"rank":21708,"time":1600.06,"first_finish":1666883462}}
     */
    private String map_type;
    private RankBean points;
    private RankBean team_rank;
    private RankBean rank;
    private Integer sort;
    private List<MapsBean> maps;

}
