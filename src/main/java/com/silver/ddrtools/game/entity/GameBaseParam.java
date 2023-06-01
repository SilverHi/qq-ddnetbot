package com.silver.ddrtools.game.entity;

import java.util.List;

import lombok.Data;

/**
 * @ClassName GuessNumberGameInfo
 * @Description TODO
 * @Author silver
 * @Date 2022/11/16 14:43
 * @Version 1.0
 **/
@Data
public class GameBaseParam {
    //游戏状态
    public final static int START = 1;
    public final static int END = 0;


    //游戏阶段
    public final static int JOIN = 1;

    public final static int GUESSNUMBER_GUESS = 2;
    public final static int GUESSNUMBER_END = 0;

    public final static int DEMOLISHBOMB_GUESS = 2;
    public final static int DEMOLISHBOMB_END = 0;

    //卧底游戏描述阶段
    public final static int WHOISUNDERCOVER_DESCRIBE = 2;
    //卧底游戏投票阶段
    public final static int WHOISUNDERCOVER_VOTE = 3;
    //卧底游戏平票描述阶段
    public final static int WHOISUNDERCOVER_PINGPAI_DESCRIBE = 4;
    //卧底游戏平票投票阶段
    public final static int WHOISUNDERCOVER_PINGPAI_VOTE = 5;
    //卧底游戏结束阶段
    public final static int WHOISUNDERCOVER_END = 0;

    private String gameName;
    private String groupId;
    private String userId;
    private String repType;
    private List<PlayerBaseParam> playerList;
    private Integer gameStatus;
    private Integer gameStep;
    //当前监听玩家
    private PlayerBaseParam currentListenPlayer;
}
