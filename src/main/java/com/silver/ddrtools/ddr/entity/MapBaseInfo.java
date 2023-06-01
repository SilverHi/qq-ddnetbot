package com.silver.ddrtools.ddr.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MapBaseInfo
 * @Description TODO
 * @Author silver
 * @Date 2022/11/20 10:42
 * @Version 1.0
 **/

@NoArgsConstructor
@Data
public class MapBaseInfo {

    /**
     * name : Koule 15
     * website : https://ddnet.org/maps/Koule-32-15
     * thumbnail : https://ddnet.org/ranks/maps/Koule_15.png
     * web_preview : https://ddnet.org/mappreview/?map=Koule+15
     * type : Oldschool
     * points : 12
     * difficulty : 2
     * mapper : Intercity, Broken & Bee
     * release : 2022-11-19 16:30
     * width : 200
     * height : 200
     * tiles : ["WEAPON_SHOTGUN","CHECKPOINT_FIRST"]
     */

    private String name;
    private String website;
    private String thumbnail;
    private String web_preview;
    private String type;
    private int points;
    private int difficulty;
    private String mapper;
    private String release;
    private int width;
    private int height;
    private List<String> tiles;
}
