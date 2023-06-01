package com.silver.ddrtools.game.entity.WhoIsUndercover;

import com.silver.ddrtools.game.entity.GameBaseParam;

import java.util.List;

import lombok.Data;

/**
 * @ClassName WhoIsUndercoverParam
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 21:03
 * @Version 1.0
 **/
@Data
public class WhoIsUndercoverParam extends GameBaseParam {
    public static final int MIN_PLAYER_NUM = 4;
    public static final int WIN_PLAYER_NUM = 3;
    //平票的玩家列表
    private List<WhoIsUndercoverPlayerParam> pingPaiPlayerList;
    //卧底词
    private String unsercoverWord;
    //平民词
    private String commonWord;
}
