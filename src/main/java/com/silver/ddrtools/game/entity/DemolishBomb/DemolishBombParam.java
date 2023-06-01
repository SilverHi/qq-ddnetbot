package com.silver.ddrtools.game.entity.DemolishBomb;

import com.silver.ddrtools.game.entity.GameBaseParam;

import lombok.Data;

/**
 * @ClassName DemolishBombParam
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 19:50
 * @Version 1.0
 **/
@Data
public class DemolishBombParam extends GameBaseParam {
    private String answer;
    //最大范围
    private Integer maxRange;
    //最小范围
    private Integer minRange;
}
