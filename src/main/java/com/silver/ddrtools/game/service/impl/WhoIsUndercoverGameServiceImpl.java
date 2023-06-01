package com.silver.ddrtools.game.service.impl;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.GameParam;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.game.dao.GameWodiMapper;
import com.silver.ddrtools.game.entity.GameBaseParam;
import com.silver.ddrtools.game.entity.GameWodi;
import com.silver.ddrtools.game.entity.PlayerBaseParam;
import com.silver.ddrtools.game.entity.WhoIsUndercover.WhoIsUndercoverParam;
import com.silver.ddrtools.game.entity.WhoIsUndercover.WhoIsUndercoverPlayerParam;
import com.silver.ddrtools.game.service.GameBaseService;
import com.silver.ddrtools.game.service.WhoIsUndercoverGameService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @ClassName WhoIsUndercoverGameServiceImpl
 * @Description TODO
 * @Author silver
 * @Date 2022/11/17 21:27
 * @Version 1.0
 **/
@Service
public class WhoIsUndercoverGameServiceImpl extends GameBaseService implements WhoIsUndercoverGameService {

    @Resource
    public GameWodiMapper gameWodiMapper;
    String gameName = "卧底";

    @Override
    public void initGame(BotMessage botMessage) {
        //不支持私聊
        if ("private".equals(messageType)) {
            BotUtil.sendPrivate(userId, "一个人咋玩儿啊？快去群里玩儿吧");
            return;
        }
        WhoIsUndercoverParam gameParam = new WhoIsUndercoverParam();
        gameParam.setUserId(userId);
        gameParam.setGroupId(groupId);
        gameParam.setRepType(messageType);
        gameParam.setPlayerList(new ArrayList<>());
        gameParam.setGameStatus(GameBaseParam.START);
        gameParam.setGameStep(GameBaseParam.JOIN);
        gameParam.setGameName(gameName);
        //从词库随机抽取词语
        Integer integer = gameWodiMapper.selectCount(null);
        if (integer == null || integer == 0) {
            BotUtil.sendGroup(groupId, "词库里没有词语，快去添加吧");
            this.unfinish = false;
            return;
        }
        int i = (int) (Math.random() * integer) + 1;
        GameWodi gameWodi = gameWodiMapper.selectById(i);
        gameParam.setUnsercoverWord(gameWodi.getWodiWork());
        gameParam.setCommonWord(gameWodi.getPingminWord());
        GameParam.gameList.add(gameParam);
        BotUtil.sendGroup(groupId, "游戏开始了，发送【/" + gameName + " 参加】来加入游戏");
        this.unfinish = false;
        return;

    }

    @Override
    public PlayerBaseParam createPlayer() {
        return new WhoIsUndercoverPlayerParam();
    }

    @Override
    public void endJoinDo(BotMessage message, GameBaseParam gameBaseParam) {
        //游戏人数要大于3人
        if (gameBaseParam.getPlayerList().size() < WhoIsUndercoverParam.MIN_PLAYER_NUM) {
            BotUtil.sendGroup(gameBaseParam.getGroupId(), "游戏人数要大于三人，不然卧底也太惨了吧");
            this.unfinish = false;
            return;
        }
        //随机分配身份
        WhoIsUndercoverParam gameParam = (WhoIsUndercoverParam) gameBaseParam;
        int i = (int) (Math.random() * gameParam.getPlayerList().size());
        List<PlayerBaseParam> playerList = gameParam.getPlayerList();

        //随机分配卧底
        for (PlayerBaseParam playerBaseParam : playerList) {
            WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
            if (player.getUserId().equals(gameParam.getPlayerList().get(i).getUserId())) {
                player.setIdentity(WhoIsUndercoverPlayerParam.UNDERCOVER);
                player.setVoteNum(0);
                //私聊通知卧底词
                BotUtil.sendGroupTempPrivate(player.getUserId(), groupId, "你是卧底，卧底词是：" + gameParam.getUnsercoverWord());
            } else {
                player.setIdentity(WhoIsUndercoverPlayerParam.COMMON);
                player.setVoteNum(0);
                //私聊通知平民词
                BotUtil.sendGroupTempPrivate(player.getUserId(), groupId, "你是平民，平民词是：" + gameParam.getCommonWord());
            }
        }
        gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_DESCRIBE);
        //设置当前玩家
        gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
        BotUtil.sendGroup(gameBaseParam.getGroupId(), "游戏开始了词语已经私聊发送给各位请查看（如果未收到可能是没有bot好友，临时会话会导致bot封号，请添加bot为好友）");
        BotUtil.sendGroup(gameBaseParam.getGroupId(), "请"+gameParam.getCurrentListenPlayer().getUserIndex()+"号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
        this.unfinish = false;
        return;
    }

    @Override
    public void gameDo(BotMessage message, GameBaseParam gameBaseParam) {
        WhoIsUndercoverParam gameParam = (WhoIsUndercoverParam) gameBaseParam;
        WhoIsUndercoverPlayerParam currentListenPlayer = (WhoIsUndercoverPlayerParam) gameParam.getCurrentListenPlayer();
        if (gameParam.getGameStep().equals(GameBaseParam.WHOISUNDERCOVER_DESCRIBE) && currentListenPlayer.getUserId().equals(userId)) {
            //发言
            currentListenPlayer.setSpeakStatus(true);
            //设置下一个玩家
            int i = gameParam.getPlayerList().indexOf(currentListenPlayer);
            if (i == gameParam.getPlayerList().size() - 1) {
                gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
            } else {
                gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(i + 1));
            }
            //判断是否所有人都发言了
            boolean allSpeak = true;
            for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                if (player.getSpeakStatus() == null || !player.getSpeakStatus()) {
                    allSpeak = false;
                    break;
                }
            }
            if (allSpeak) {
                //所有人都发言了，进入投票环节
                gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_VOTE);
                //清除所有人的发言状态
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    player.setSpeakStatus(false);
                }
                BotUtil.sendGroup(gameParam.getGroupId(), "所有人都发言了，进入投票环节输入【/" + gameName + " {玩家编号}】来投票");
                BotUtil.sendGroup(gameParam.getGroupId(), "请"+gameParam.getCurrentListenPlayer().getUserIndex()+"号【" + gameParam.getCurrentListenPlayer().getUserName() + "】投票");
                this.unfinish = false;
                return;
            } else {
                BotUtil.sendGroup(gameBaseParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号玩家【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                this.unfinish = false;
                return;
            }
        }
        //投票
        if (gameParam.getGameStep().equals(GameBaseParam.WHOISUNDERCOVER_VOTE) && currentListenPlayer.getUserId().equals(userId)) {
            //投票
            //判断是否是数字
            if (!BotUtil.isNumeric(userInput)) {
                BotUtil.sendGroup(gameParam.getGroupId(), "请输入正确的玩家编号");
                this.unfinish = false;
                return;
            }
            //判断是否是玩家编号
            int userIndex = Integer.parseInt(userInput);
            if (userIndex > gameParam.getPlayerList().size()) {
                BotUtil.sendGroup(gameParam.getGroupId(), "请输入正确的玩家编号");
                this.unfinish = false;
                return;
            }
            //判断是否是自己
            if (userIndex == currentListenPlayer.getUserIndex()) {
                BotUtil.sendGroup(gameParam.getGroupId(), "不能投自己");
                this.unfinish = false;
                return;
            }

            WhoIsUndercoverPlayerParam votePlayer = (WhoIsUndercoverPlayerParam) gameParam.getPlayerList().get(userIndex - 1);
            votePlayer.setVoteNum(votePlayer.getVoteNum() + 1);

            //设置投票人的投票状态
            currentListenPlayer.setVoteStatus(true);
            //设置下一个玩家
            int i = gameParam.getPlayerList().indexOf(currentListenPlayer);
            if (i == gameParam.getPlayerList().size() - 1) {
                gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
            } else {
                gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(i + 1));
            }
            //判断是否所有人都投票了
            boolean allVote = true;
            for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                if (player.getVoteStatus() == null || player.getVoteStatus() == false) {
                    allVote = false;
                    break;
                }
            }
            if (allVote) {

                //获取每个玩家的票数
                StringBuilder sb = new StringBuilder();
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    sb.append(player.getUserIndex()).append("号玩家【").append(player.getUserName()).append("】").append(player.getVoteNum()).append("票").append("\n");
                }
                //发送投票结果
                BotUtil.sendGroup(gameParam.getGroupId(), "投票结果如下：\n" + sb.toString());

                //计算投票结果
                int maxVoteNum = 0;
                List<WhoIsUndercoverPlayerParam> maxVotePlayerList = new ArrayList<>();
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getVoteNum() > maxVoteNum) {
                        maxVoteNum = player.getVoteNum();
                        maxVotePlayerList.clear();
                        maxVotePlayerList.add(player);
                    } else if (player.getVoteNum() == maxVoteNum) {
                        maxVotePlayerList.add(player);
                    }
                }
                //判断是否有平票
                if (maxVotePlayerList.size() > 1) {
                    //有平票,平票玩家重新发言
                    gameParam.setPingPaiPlayerList(maxVotePlayerList);
                    gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_PINGPAI_DESCRIBE);
                    //清除所有人的投票状态
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        player.setVoteStatus(false);
                        player.setVoteNum(0);
                    }
                    //设置平票玩家的发言状态
                    for (PlayerBaseParam playerBaseParam : maxVotePlayerList) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        player.setSpeakStatus(false);
                    }
                    //平票玩家名称
                    String pingpaiPlayerName = "";
                    for (PlayerBaseParam playerBaseParam : maxVotePlayerList) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        pingpaiPlayerName += "【"+player.getUserName() + "】";
                    }
                    //发送投票结果
                    BotUtil.sendGroup(gameParam.getGroupId(), "玩家" + pingpaiPlayerName + "平票,需要重新发言");

                    //将未发言的玩家设置为当前监听玩家
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        if (player.getSpeakStatus() == null || !player.getSpeakStatus()) {
                            gameParam.setCurrentListenPlayer(player);
                            break;
                        }
                    }
                    //请玩家发言
                    BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                    this.unfinish = false;
                    return;
                } else {
                    //没有平票
                    WhoIsUndercoverPlayerParam maxVotePlayer = (WhoIsUndercoverPlayerParam) maxVotePlayerList.get(0);
                    //判断是否是卧底
                    if (maxVotePlayer.getIdentity().equals(WhoIsUndercoverPlayerParam.UNDERCOVER)) {
                        //是卧底,游戏结束
                        gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_END);
                        //发送游戏结束
                        BotUtil.sendGroup(gameParam.getGroupId(), "游戏结束,平民胜利，卧底是" + maxVotePlayer.getUserIndex() + "号玩家【" + maxVotePlayer.getUserName() + "】，卧底词是" + gameParam.getUnsercoverWord());
                        GameParam.gameList.remove(gameBaseParam);
                        this.unfinish = false;
                        return;
                    } else {
                        //不是卧底,把玩家移除
                        gameParam.getPlayerList().remove(maxVotePlayer);
                        //通知玩家
                        BotUtil.sendGroup(gameParam.getGroupId(),   maxVotePlayer.getUserIndex() + "号【" + maxVotePlayer.getUserName() + "】是平民，被投票出局，游戏继续");
                        gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_DESCRIBE);

                        //判断剩余玩家小于3人
                        if (gameParam.getPlayerList().size() <= WhoIsUndercoverParam.WIN_PLAYER_NUM) {
                            //游戏结束
                            gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_END);
                            //找出卧底玩家
                            PlayerBaseParam undercoverPlayer = null;
                            for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                                WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                                if (player.getIdentity().equals(WhoIsUndercoverPlayerParam.UNDERCOVER)) {
                                    undercoverPlayer = player;
                                    break;
                                }
                            }
                            //发送游戏结束
                            BotUtil.sendGroup(gameParam.getGroupId(), "游戏结束,卧底胜利，卧底是" + undercoverPlayer.getUserIndex() + "号玩家【" + undercoverPlayer.getUserName() + "】，卧底词是" + gameParam.getUnsercoverWord());
                            GameParam.gameList.remove(gameBaseParam);
                            this.unfinish = false;
                            return;
                        }
                        //清除所有人的状态
                        for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                            WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                            player.setVoteStatus(false);
                            player.setVoteNum(0);
                            player.setSpeakStatus(false);
                        }
                        //设置下一个玩家
                        gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
                        //请玩家发言
                        BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                        this.unfinish = false;
                        return;
                    }
                }

            }else {
                //下一个玩家投票
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getVoteStatus() == null || !player.getVoteStatus()) {
                        gameParam.setCurrentListenPlayer(player);
                        break;
                    }
                }
                //请玩家投票
                BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】投票");
            }
        }

        //平票发言
        if (gameParam.getGameStep().equals(GameBaseParam.WHOISUNDERCOVER_PINGPAI_DESCRIBE)) {
            //判断是否是当前玩家
            if (gameParam.getCurrentListenPlayer().getUserId().equals(userId)) {
                //设置发言状态
                currentListenPlayer.setSpeakStatus(true);
                //判断是否所有玩家都发言了
                boolean allSpeak = true;
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getSpeakStatus() == null || !player.getSpeakStatus()) {
                        allSpeak = false;
                        break;
                    }
                }
                if (allSpeak) {
                    //所有玩家都发言了,进入投票环节
                    gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_PINGPAI_VOTE);
                    //清除所有人的状态
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        player.setVoteStatus(false);
                        player.setVoteNum(0);
                        player.setSpeakStatus(false);
                    }
                    //设置下一个玩家
                    gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
                    //请玩家投票
                    BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】投票");
                    this.unfinish = false;
                    return;
                }else {
                    //找还没有发言的玩家
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        if (player.getSpeakStatus() == null || !player.getSpeakStatus()) {
                            //设置下一个玩家
                            gameParam.setCurrentListenPlayer(player);
                            break;
                        }
                    }
                    //请玩家发言
                    BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                    this.unfinish = false;
                    return;
                }
            }
        }

        //平票投票
        if (gameParam.getGameStep().equals(GameBaseParam.WHOISUNDERCOVER_PINGPAI_VOTE)) {
            //判断是否是当前玩家
            if (gameParam.getCurrentListenPlayer().getUserId().equals(userId)) {
                //判断是否是数字
                if (!BotUtil.isNumeric(userInput)) {
                    BotUtil.sendGroup(gameParam.getGroupId(), "请输入正确的玩家编号");
                    this.unfinish = false;
                    return;
                }
                //判断是否是平票的玩家编号
                boolean isPingpai = false;
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getUserIndex().equals(Integer.valueOf(userInput))) {
                        isPingpai = true;
                        break;
                    }
                }
                if (!isPingpai) {
                    BotUtil.sendGroup(gameParam.getGroupId(), "请输入平票玩家的编号");
                    this.unfinish = false;
                    return;
                }
                //判断是否是自己
                if (userInput == currentListenPlayer.getUserIndex()+"") {
                    BotUtil.sendGroup(gameParam.getGroupId(), "不能投自己");
                    this.unfinish = false;
                    return;
                }
                //获取被投票的玩家
                WhoIsUndercoverPlayerParam votePlayer = null;
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getUserIndex().equals(Integer.valueOf(userInput))) {
                        votePlayer = player;
                        break;
                    }
                }
                //设置投票数
                votePlayer.setVoteNum(votePlayer.getVoteNum() + 1);
                //设置投票状态
                currentListenPlayer.setVoteStatus(true);
                //判断是否所有玩家都投票了
                boolean allVote = true;
                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                    if (player.getVoteStatus() == null || !player.getVoteStatus()) {
                        allVote = false;
                        break;
                    }
                }
                if (allVote) {
                    //获取平票玩家投票数
                    StringBuilder sb = new StringBuilder();
                    for (PlayerBaseParam playerBaseParam : gameParam.getPingPaiPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        sb.append(playerBaseParam.getUserIndex()).append("号【").append(player.getUserName()).append("】").append(player.getVoteNum()).append("票").append("\n");
                    }
                    //通知平票玩家投票结果
                    BotUtil.sendGroup(gameParam.getGroupId(), "平票玩家投票结果如下:\n" + sb.toString());
                    //是否有平票玩家
                    //计算投票结果
                    int maxVoteNum = 0;
                    List<WhoIsUndercoverPlayerParam> maxVotePlayerList = new ArrayList<>();
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        if (player.getVoteNum() > maxVoteNum) {
                            maxVoteNum = player.getVoteNum();
                            maxVotePlayerList.clear();
                            maxVotePlayerList.add(player);
                        } else if (player.getVoteNum() == maxVoteNum) {
                            maxVotePlayerList.add(player);
                        }
                    }
                    //判断是否有平票
                    if (maxVotePlayerList.size() > 1) {
                        //有平票
                        //设置平票玩家
                        gameParam.setPingPaiPlayerList(maxVotePlayerList);
                        //设置游戏步骤
                        gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_PINGPAI_DESCRIBE);
                        //设置平票玩家发言状态
                        for (PlayerBaseParam playerBaseParam : gameParam.getPingPaiPlayerList()) {
                            WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                            player.setSpeakStatus(false);
                        }

                        for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                            WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                            if (player.getSpeakStatus() == null || !player.getSpeakStatus()) {
                                //设置下一个玩家
                                gameParam.setCurrentListenPlayer(player);
                                break;
                            }
                        }
                        //请玩家发言
                        BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                        this.unfinish = false;
                        return;
                    } else {
                        //没有平票
                        WhoIsUndercoverPlayerParam maxVotePlayer = maxVotePlayerList.get(0);
                        //判断是否是卧底
                        if (maxVotePlayer.getIdentity().equals(WhoIsUndercoverPlayerParam.UNDERCOVER)) {
                            //是卧底,游戏结束
                            gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_END);
                            //发送游戏结束
                            BotUtil.sendGroup(gameParam.getGroupId(), "游戏结束,平民胜利，卧底是" + maxVotePlayer.getUserIndex() + "号玩家【" + maxVotePlayer.getUserName() + "】，卧底词是" + gameParam.getUnsercoverWord());
                            GameParam.gameList.remove(gameBaseParam);
                            this.unfinish = false;
                            return;
                        } else {
                            //不是卧底,把玩家移除
                            gameParam.getPlayerList().remove(maxVotePlayer);
                            //通知玩家
                            BotUtil.sendGroup(gameParam.getGroupId(),   maxVotePlayer.getUserIndex() + "号【" + maxVotePlayer.getUserName() + "】是平民，被投票出局，游戏继续");
                            gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_DESCRIBE);

                            //判断剩余玩家小于3人
                            if (gameParam.getPlayerList().size() <= WhoIsUndercoverParam.WIN_PLAYER_NUM) {
                                //游戏结束
                                gameParam.setGameStep(GameBaseParam.WHOISUNDERCOVER_END);
                                //找出卧底玩家
                                PlayerBaseParam undercoverPlayer = null;
                                for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                                    WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                                    if (player.getIdentity().equals(WhoIsUndercoverPlayerParam.UNDERCOVER)) {
                                        undercoverPlayer = player;
                                        break;
                                    }
                                }
                                //发送游戏结束
                                BotUtil.sendGroup(gameParam.getGroupId(), "游戏结束,卧底胜利，卧底是" + undercoverPlayer.getUserIndex() + "号玩家【" + undercoverPlayer.getUserName() + "】，卧底词是" + gameParam.getUnsercoverWord());
                                GameParam.gameList.remove(gameBaseParam);
                                this.unfinish = false;
                                return;
                            }
                            //清除所有人的状态
                            for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                                WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                                player.setVoteStatus(false);
                                player.setVoteNum(0);
                                player.setSpeakStatus(false);
                            }
                            //设置下一个玩家
                            gameParam.setCurrentListenPlayer(gameParam.getPlayerList().get(0));
                            //请玩家发言
                            BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】发言");
                            this.unfinish = false;
                            return;
                        }
                    }

                }else {
                    //下一个玩家投票
                    for (PlayerBaseParam playerBaseParam : gameParam.getPlayerList()) {
                        WhoIsUndercoverPlayerParam player = (WhoIsUndercoverPlayerParam) playerBaseParam;
                        if (player.getVoteStatus() == null || !player.getVoteStatus()) {
                            gameParam.setCurrentListenPlayer(player);
                            break;
                        }
                    }
                    //请玩家投票
                    BotUtil.sendGroup(gameParam.getGroupId(), "请" + gameParam.getCurrentListenPlayer().getUserIndex() + "号【" + gameParam.getCurrentListenPlayer().getUserName() + "】投票");
                }
            }
        }
    }

    @Override
    public void endGameDo(BotMessage message, GameBaseParam gameBaseParam) {

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
