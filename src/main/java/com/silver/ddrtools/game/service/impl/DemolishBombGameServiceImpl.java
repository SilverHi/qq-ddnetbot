package com.silver.ddrtools.game.service.impl;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.GameParam;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.game.entity.DemolishBomb.DemolishBombParam;
import com.silver.ddrtools.game.entity.GameBaseParam;
import com.silver.ddrtools.game.entity.PlayerBaseParam;
import com.silver.ddrtools.game.service.DemolishBombGameService;
import com.silver.ddrtools.game.service.GameBaseService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DemolishBombGameServiceImpl
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 17:01
 * @Version 1.0
 **/
@Service
@Slf4j
public class DemolishBombGameServiceImpl extends GameBaseService implements DemolishBombGameService {
    public String gameName = "拆炸弹";

    @Override
    public void initGame(BotMessage botMessage) {
        //不支持私聊
        if ("private".equals(messageType)) {
            BotUtil.sendPrivate(userId, "拆炸弹一个人咋玩儿啊，自己炸自己？快去群里玩儿吧");
            return;
        }
        DemolishBombParam gameParam = new DemolishBombParam();
        gameParam.setUserId(userId);
        gameParam.setGroupId(groupId);
        gameParam.setRepType(messageType);
        gameParam.setPlayerList(new ArrayList<>());
        gameParam.setGameStatus(GameBaseParam.START);
        gameParam.setGameStep(GameBaseParam.JOIN);
        gameParam.setGameName(gameName);
        gameParam.setMaxRange(100);
        gameParam.setMinRange(0);
        //生成1-99的随机数
        int random = (int) (Math.random() * 99 + 1);
        gameParam.setAnswer(String.valueOf(random));
        GameParam.gameList.add(gameParam);
        BotUtil.sendGroup(groupId, "游戏开始了，发送【/"+gameName+" 参加】来加入游戏,发送【/"+gameName+" 结束报名】来开始游戏");
        this.unfinish = false;
        return;
    }

    @Override
    public PlayerBaseParam createPlayer() {
        return new PlayerBaseParam();
    }

    @Override
    public void endJoinDo(BotMessage message, GameBaseParam gameBaseParam) {
        gameBaseParam.setGameStep(GameBaseParam.DEMOLISHBOMB_GUESS);
        gameBaseParam.setCurrentListenPlayer(gameBaseParam.getPlayerList().get(0));
        //剧情说明todo
//            BotUtil.sendGroup(message.getGroup_id(), "我这里有编号1-99共99个箱子，每个玩家需要选择一个箱子");

        BotUtil.sendGroup(message.getGroup_id(), "游戏开始了，请【" + gameBaseParam.getCurrentListenPlayer().getUserName() + "】开始拆炸弹");
        this.unfinish = false;
        return;
    }

    @Override
    public void gameDo(BotMessage message, GameBaseParam gameBaseParam) {
        DemolishBombParam gameParam = (DemolishBombParam)gameBaseParam;
        if (gameParam.getGameStep() == GameBaseParam.DEMOLISHBOMB_GUESS && userId.equals(gameParam.getCurrentListenPlayer().getUserId()) ) {
            if (gameParam.getCurrentListenPlayer().getUserId().equals(userId)) {
                //判断输入是否为数字
                if (!userInput.matches("[0-9]+")) {
                    BotUtil.sendGroup(message.getGroup_id(), "请输入数字");
                    this.unfinish = false;
                    return;
                }
                //判断输入是否为最小值到最大值之间
                if (Integer.parseInt(userInput) <= gameParam.getMinRange() || Integer.parseInt(userInput) >= gameParam.getMaxRange()) {
                    BotUtil.sendGroup(message.getGroup_id(), "请输入" + gameParam.getMinRange() + "-" + gameParam.getMaxRange() + "之间的数字");
                    this.unfinish = false;
                    return;
                }
                //判断是否猜中
                if (Integer.valueOf(userInput) == Integer.valueOf(gameParam.getAnswer())) {
                    BotUtil.sendGroup(message.getGroup_id(), "恭喜【" + gameParam.getCurrentListenPlayer().getUserName() + "】被炸飞了，炸弹是" + gameParam.getAnswer());
                    GameParam.gameList.remove(gameParam);
                    this.unfinish = false;
                    return;
                }
                //判断是否猜错
                if (Integer.valueOf(userInput) != Integer.valueOf(gameParam.getAnswer())) {
                    //比较大小修改范围
                    if (Integer.parseInt(userInput) > Integer.parseInt(gameParam.getAnswer())) {
                        gameParam.setMaxRange(Integer.parseInt(userInput));
                    } else {
                        gameParam.setMinRange(Integer.parseInt(userInput));
                    }
                    BotUtil.sendGroup(message.getGroup_id(), "【" + gameParam.getCurrentListenPlayer().getUserName() + "】活了下来,炸弹在" + gameParam.getMinRange() + "-" + gameParam.getMaxRange() + "之间");

                    //设置下一个玩家
                    int nextPlayerIndex = gameParam.getCurrentListenPlayer().getUserIndex() + 1;
                    if (nextPlayerIndex > gameParam.getPlayerList().size()) {
                        nextPlayerIndex = 1;
                    }
                    gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(nextPlayerIndex - 1));
                    BotUtil.sendGroup(message.getGroup_id(), "请【" + gameParam.getCurrentListenPlayer().getUserName() + "】开始拆炸弹");
                    this.unfinish = false;
                    return;
                }
            }

        }


    }

    @Override
    public void endGameDo(BotMessage message, GameBaseParam gameBaseParam) {
        DemolishBombParam gameParam = (DemolishBombParam)gameBaseParam;
        BotUtil.sendGroup(message.getGroup_id(), "游戏结束了，炸弹是" + gameParam.getAnswer());
        this.unfinish = false;
        return;
    }

    @Override
    public String getGameName() {

        return gameName;
    }

    @Override
    public void execute(BotMessage message, String cmd) {
        game(message);
    }
}
