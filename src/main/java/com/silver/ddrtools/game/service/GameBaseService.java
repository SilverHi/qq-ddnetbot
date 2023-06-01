package com.silver.ddrtools.game.service;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.GameParam;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.ddr.service.BeasService;
import com.silver.ddrtools.game.entity.GameBaseParam;
import com.silver.ddrtools.game.entity.PlayerBaseParam;

/**
 * @ClassName GameBaseService
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 21:37
 * @Version 1.0
 **/

public abstract class GameBaseService implements BeasService {
    public String messageType;
    public String userId ;
    public String groupId ;
    public String userInput;
    public boolean unfinish;

    public void getInputBaseInfo(BotMessage message){
        userInput = message.getRaw_message().trim().replace("/"+getGameName(), "").trim();
        messageType = message.getMessage_type();
        groupId = message.getGroup_id() == null ? "" : message.getGroup_id();
        userId = message.getUser_id() == null ? "" : message.getUser_id();
        unfinish = true;
    }

    public  void game(BotMessage message){
        getInputBaseInfo(message);
        GameBaseParam gameBaseParam = checkGame();
        if(unfinish){
            startGame(message, gameBaseParam);
        }
        if(unfinish){
            joinGame(message, gameBaseParam);
        }
        if (gameBaseParam != null&& unfinish){
            gameDo(message, gameBaseParam);
        }
        if (unfinish){
            endGame(message, gameBaseParam);
        }
    };

    //检查是否有游戏正在进行
    public GameBaseParam checkGame() {
        GameBaseParam gameBaseParam = null;
        for (GameBaseParam info : GameParam.gameList) {
            if (groupId.equals(info.getGroupId()) && BotMessage.GROUP.equals(info.getRepType()) && getGameName().equals(info.getGameName())) {
                gameBaseParam = info;
            }
            if (userId.equals(info.getUserId()) && BotMessage.PRIVATE.equals(info.getRepType()) && getGameName().equals(info.getGameName())) {
                gameBaseParam = info;
            }
        }
        return gameBaseParam;
    }

    //开始游戏
    public void startGame(BotMessage botMessage, GameBaseParam gameBaseParam){
        if ("开始".equals(userInput)){
            if (gameBaseParam == null) {
                initGame(botMessage);
            }else {
            //游戏已经开始了
                BotUtil.autoSwitchSend(messageType, groupId, userId, "游戏已经开始了");
                unfinish = false;
                return;
            }

        }
    }

    public abstract void initGame(BotMessage botMessage);

    //加入游戏
    public void joinGame(BotMessage message, GameBaseParam gameBaseParam){
        //参加游戏
        if ("参加".equals(userInput)) {
            if (gameBaseParam == null) {
                BotUtil.sendGroup(groupId, "游戏还没开始呢，发送【/"+getGameName()+" 开始】来开始游戏");
                this.unfinish = false;
                return;
            }
            if (gameBaseParam.getGameStep() != GameBaseParam.JOIN) {
                BotUtil.sendGroup(groupId, "游戏已经开始了，不可以中途加入哦");
                this.unfinish = false;
                return;
            }
            for (PlayerBaseParam playerBaseParam : gameBaseParam.getPlayerList()) {
                if (playerBaseParam.getUserId().equals(userId)) {
                    BotUtil.sendGroup(groupId, "你已经参加了游戏了，别重复参加了");
                    this.unfinish = false;
                    return;
                }
            }
            PlayerBaseParam player = createPlayer();
            player.setUserId(userId);
            player.setUserName(message.getSender().getCard() == null ||"".equals(message.getSender().getCard()) ? message.getSender().getNickname() : message.getSender().getCard());
            player.setUserIndex(gameBaseParam.getPlayerList().size() + 1);
            gameBaseParam.getPlayerList().add(player);
            BotUtil.sendGroup(message.getGroup_id(), "玩家【" + player.getUserName() + "】加入了游戏");
            this.unfinish = false;
            return;
        }
        endJoinGame(message, gameBaseParam);
    }

    public abstract PlayerBaseParam createPlayer();


    //结束报名
    public void endJoinGame(BotMessage message, GameBaseParam gameBaseParam){
        if ("结束报名".equals(userInput)) {
            if (gameBaseParam == null) {
                BotUtil.sendGroup(groupId, "游戏还没开始呢，发送【/"+getGameName()+" 开始】来开始游戏");
                this.unfinish = false;
                return;
            }
            if (gameBaseParam.getGameStep() != GameBaseParam.JOIN) {
                BotUtil.sendGroup(groupId, "啥时候了你结束报名，游戏都开始了");
                this.unfinish = false;
                return;
            }
            //发送人数
            BotUtil.sendGroup(message.getGroup_id(), "当前玩家数量为【" + gameBaseParam.getPlayerList().size() + "】");
            endJoinDo(message,gameBaseParam);
            return;
        }
    }

    public abstract void endJoinDo(BotMessage message, GameBaseParam gameBaseParam);

    public abstract void gameDo(BotMessage message, GameBaseParam gameBaseParam);

    //结束游戏
    public void endGame(BotMessage message, GameBaseParam gameBaseParam){
        if ("结束".equals(userInput)) {
            if (gameBaseParam == null) {
                BotUtil.sendGroup(message.getGroup_id(), "游戏还没开始呢，发送【/拆炸弹 开始】来开始游戏");
                this.unfinish = false;
                return;
            }
            endGameDo(message,gameBaseParam);
            GameParam.gameList.remove(gameBaseParam);
            return;
        }
    }

    public abstract void endGameDo(BotMessage message, GameBaseParam gameBaseParam);

    public abstract String getGameName();

}
