package com.silver.ddrtools.ddr.entity;

import java.util.List;

import lombok.Data;

/**
 * @ClassName TypesBean
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 22:51
 * @Version 1.0
 **/
@Data
public class TypesBean {

    /**
     * Novice : {"points":{"total":486,"points":467,"rank":4124},"team_rank":{"rank":"unranked"},"rank":{"rank":"unranked"},"maps":{"Give u the Moon":{"points":3,"total_finishes":25490,"finishes":1,"rank":7058,"time":406.34,"first_finish":1664970449},"Teleport":{"points":3,"total_finishes":22092,"finishes":1,"rank":15382,"time":1264.28,"first_finish":1666514755},"xyz_ddrace2":{"points":3,"total_finishes":31314,"finishes":1,"rank":21708,"time":1600.06,"first_finish":1666883462}}}
     */

    //简单图
    private List<MapInfoBean> maplist;
//    //中阶图
//    private MapInfoBean Moderate;
//    //高阶图
//    private MapInfoBean Brutal;
//    //疯狂图
//    private MapInfoBean Insane;
//    //分身图
//    private MapInfoBean Dummy;
//    //古典简单图
//    private MapInfoBean DDmaXEasy;
//    //古典中阶图
//    private MapInfoBean DDmaXNext;
//    //古典高阶图
//    private MapInfoBean DDmaXPro;
//    //古典疯狂图
//    private MapInfoBean DDmaXNut;
//    //传统图
//    private MapInfoBean Oldschool;
//    //solo图
//    private MapInfoBean Solo;
//    //竞速图
//    private MapInfoBean Race;
//    //娱乐图
//    private MapInfoBean Fun;


}
