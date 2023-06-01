package com.silver.ddrtools.game.entity.WhoIsUndercover;

import com.silver.ddrtools.game.entity.PlayerBaseParam;

import lombok.Data;

/**
 * @ClassName WhoIsUndercoverPlayerParam
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 21:15
 * @Version 1.0
 **/
@Data
public class WhoIsUndercoverPlayerParam extends PlayerBaseParam {
    public static final String UNDERCOVER = "卧底";
    public static final String COMMON = "平民";
    //玩家的身份
    private String identity;
    //玩家的状态
    private String status;
    //发言状态
    private Boolean speakStatus;
    //玩家的投票
    private Integer voteNum;
    //玩家的投票状态
    private Boolean voteStatus;

}
