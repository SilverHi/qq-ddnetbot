package com.silver.ddrtools.game.service.impl;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.GameParam;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.game.dao.GameWodiMapper;
import com.silver.ddrtools.game.entity.GuessNumber.GuessNumberParam;
import com.silver.ddrtools.game.entity.GameBaseParam;
import com.silver.ddrtools.game.entity.PlayerBaseParam;
import com.silver.ddrtools.game.service.GuessNumberGameService;

import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TbBookinfoServiceImpl
 * @Description TODO
 * @Author silver
 * @Date 2022/7/17 16:02
 * @Version 1.0
 **/
@Service
@Slf4j
public class GuessNumberGameServiceImpl implements GuessNumberGameService {


    @Resource
    public GameWodiMapper gameWodiMapper;



    LinkedList<String> plateList = new LinkedList<>();
    List<Map<String, String>> playerList = new ArrayList<>();
    List<Map<String, String>> pingpiaoplayerList = new ArrayList<>();

    final int NOTSTART = 0;
    final int JOIN = 1;
    final int YAZHU = 2;
    final int FIRSTDEAL = 3;
    final int BAOXIAN = 4;
    final int SECONDDEAL = 5;
    final int DEAL = 6;

    final int ZHADANSTART = 99;
    final int WODISTART = 100;
    final int MIAOSHU = 101;
    final int PINGPIAOMIAOSHU = 102;
    final int VOTE = 103;
    final int PINGPIAOVOTE = 104;

    final int END = 0;
    boolean gamestart = false;
    int gamestep = NOTSTART;
    int lun = 1;
    String zhuangjiaanpoker = "";
    String zhuangjiamingpoker = "";

    int zhadan = 0;
    int CurrentPlayerIndex = 0;
    String CurrentPlayer = "";

    int maxnum = 100;
    int minnum = 1;


//    @Override
//    public void play21(JSONObject jsonParam) {
//
//        int messagetype = BotUtil.PRIVATE;
//        String code = "";
//        if ("group".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.QUN;
//            code = jsonParam.getString("group_id");
//        } else if ("private".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.PRIVATE;
//            code = jsonParam.getString("user_id");
//        } else {
//            return;
//        }
//
//
//        if ("message".equals(jsonParam.getString("post_type"))) {
//            String message = jsonParam.getString("message");
//            String card = jsonParam.getJSONObject("sender").getString("card");
//            if (card == null || "".equals(card)) {
//                card = jsonParam.getJSONObject("sender").getString("nickname");
//            }
//
//            if ("开始玩21点".equals(message)) {
//                this.initPoker();
//                gamestart = true;
//
//                gamestep = JOIN;
//            }
//            if ("结束玩21点".equals(message)) {
//                gamestep = END;
//                lun=1;
//                playerList.clear();
//                gamestart = false;
//            }
//
//
//            if (gamestart) {
//                if (message.startsWith("/21")) {
//                    String inputmessage = message.replace("/21", "");
//                    if ("参加".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        Map<String, String> player = new HashMap<>();
//                        player.put("name", card);
//                        player.put("money", "100");
//                        playerList.add(player);
//                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】已加入游戏");
//                        HttpRequestUtil.doGet(url);
//                    }
//                }
//                if (message.startsWith("/21")) {
//                    String inputmessage = message.replace("/21", "");
//
//                    if ("结束报名".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        String url = BotUtil.getUrl(messagetype, code, "报名结束，目前参与人数：" + playerList.size() + "人");
//                        HttpRequestUtil.doGet(url);
//                        url = BotUtil.getUrl(messagetype, code, "每人初始筹码为100，押注最少金额为1，输入 /21 查询筹码 可查询自己的筹码  输入/21 看牌 可看自己的牌");
//                        HttpRequestUtil.doGet(url);
//                        url = BotUtil.getUrl(messagetype, code, "输入 /21 押注 金额 可押注 （押注阶段可重复输入指令修改当前押注金额）,所有玩家押注完毕后开始发牌");
//                        HttpRequestUtil.doGet(url);
//                        url = BotUtil.getUrl(messagetype, code, "进入押注阶段");
//                        HttpRequestUtil.doGet(url);
//                        gamestep = YAZHU;
//                    }
//
//                    if (inputmessage.trim().startsWith("查询筹码") && gamestep > 0) {
//                        for (Map<String, String> player : playerList) {
//                            if (player.get("name").equals(card)) {
//                                String url = BotUtil.getUrl(messagetype, code, "【" + card + "】当前筹码为：" + player.get("money"));
//                                HttpRequestUtil.doGet(url);
//                            }
//                        }
//                    }
//
//                    if (inputmessage.trim().startsWith("看牌") && gamestep > 1) {
//                        for (Map<String, String> player : playerList) {
//                            if (player.get("name").equals(card)) {
//                                String url = BotUtil.getUrl(messagetype, code, "【" + card + "】当前牌为：" + player.get("poker"));
//                                HttpRequestUtil.doGet(url);
//                            }
//                        }
//                    }
//                }
//
//
//                if (message.startsWith("/21")) {
//                    String inputmessage = message.replace("/21", "");
//                    if (inputmessage.trim().startsWith("押注") && gamestep == YAZHU) {
//                        String yazhu = inputmessage.replace("押注", "").trim();
//                        //判断money是否为数字
//                        if (yazhu.matches("[0-9]+")) {
//                            boolean find = false;
//                            for (Map<String, String> player : playerList) {
//                                if (card.equals(player.get("name"))) {
//                                    find = true;
//                                    //押注金额是否大于当前筹码
//                                    boolean isyazhu = Integer.parseInt(yazhu) <= Integer.parseInt(player.get("money"));
//                                    if (!isyazhu) {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】押注失败，押注金额大于当前筹码");
//                                        HttpRequestUtil.doGet(url);
//                                        return;
//                                    }
//                                    //是否已经押注
//                                    if (player.get("yazhu") != null) {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】将押注金额从" + player.get("yazhu") + "，修改为" + yazhu);
//                                        HttpRequestUtil.doGet(url);
//                                        player.put("yazhu", yazhu);
//                                    } else {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】押注" + yazhu);
//                                        HttpRequestUtil.doGet(url);
//                                        player.put("yazhu", yazhu);
//                                    }
//                                }
//                            }
//                            if (!find) {
//                                String url = BotUtil.getUrl(messagetype, code, "【" + card + "】未参加游戏");
//                                HttpRequestUtil.doGet(url);
//                            }
//                        } else {
//                            String url = BotUtil.getUrl(messagetype, code, "【" + card + "】输入金额有误");
//                            HttpRequestUtil.doGet(url);
//                        }
//
//                        //检查是否所有人都押注完毕
//                        if (checkYazhu()) {
//                            //发牌
//                            String url = BotUtil.getUrl(messagetype, code, "所有玩家押注完毕，开始发牌");
//                            HttpRequestUtil.doGet(url);
//                            gamestep = FIRSTDEAL;
//                            dealFirstPoker(messagetype, code);
//                            zhuangjiamingpoker += " " + plateList.pop();
//                            url = BotUtil.getUrl(messagetype, code, "庄家牌为：" + zhuangjiamingpoker + "暗牌");
//                            HttpRequestUtil.doGet(url);
//                            zhuangjiaanpoker += " " + plateList.pop();
//                            HttpRequestUtil.doGet(url);
//                            //检查庄家明牌是否为A
//                            if (zhuangjiamingpoker.contains("A")) {
//                                //庄家明牌为A，询问是否要买保险
//                                url = BotUtil.getUrl(messagetype, code, "庄家明牌为A，是否要买保险？输入 /21 买保险 可买保险，输入 /21 不买保险 可不买保险");
//                                HttpRequestUtil.doGet(url);
//                                gamestep = BAOXIAN;
//                            } else {
//                                gamestep = SECONDDEAL;
//                                url = BotUtil.getUrl(messagetype, code, "进入询问环节 \n玩家可以输入【/21 要牌】 选择是否要牌 \n输入【/21 双倍押注】 是否要双倍押注  \n输入【/21 停牌】 是否要停牌");
//                                HttpRequestUtil.doGet(url);
//                            }
//
//
//                        }
//                    } else if (inputmessage.trim().startsWith("押注") && gamestep != YAZHU) {
//                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】当前不是押注阶段");
//                        HttpRequestUtil.doGet(url);
//                    }
//
//                    if (inputmessage.trim().endsWith("保险") && gamestep == BAOXIAN) {
//                        if (inputmessage.trim().startsWith("买")) {
//                            //买保险
//                            for (Map<String, String> player : playerList) {
//                                if (card.equals(player.get("name"))) {
//                                    //是否已经押注
//                                    if (player.get("baoxian") != null) {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】已经买过保险");
//                                        HttpRequestUtil.doGet(url);
//                                    } else {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】买保险");
//                                        HttpRequestUtil.doGet(url);
//                                        player.put("baoxian", "1");
//                                        //扣除保险金额
//                                        int money = Integer.parseInt(player.get("money"));
//                                        int yazhu = Integer.parseInt(player.get("yazhu"));
//                                        player.put("money", String.valueOf(money - yazhu / 2));
//                                    }
//                                }
//                            }
//
//
//                        } else {
//                            //不买保险
//                            for (Map<String, String> player : playerList) {
//                                if (card.equals(player.get("name"))) {
//                                    //是否已经押注
//                                    if (player.get("baoxian") != null) {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】已经买过保险");
//                                        HttpRequestUtil.doGet(url);
//                                    } else {
//                                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】不买保险");
//                                        HttpRequestUtil.doGet(url);
//                                        player.put("baoxian", "0");
//                                    }
//                                }
//                            }
//                        }
//
//                        //检查是否所有人都买保险完毕
//                        if (checkBaoxian()) {
//                            //发牌
//                            String url = BotUtil.getUrl(messagetype, code, "所有玩家买保险完毕，庄家查看暗牌确认是否有黑杰克");
//                            HttpRequestUtil.doGet(url);
//
//                            if (checkZhuangjiaBlackJack(zhuangjiaanpoker)) {
//                                //庄家有黑杰克
//                                url = BotUtil.getUrl(messagetype, code, "庄家暗牌为：" + zhuangjiaanpoker);
//                                HttpRequestUtil.doGet(url);
//                                url = BotUtil.getUrl(messagetype, code, "庄家有黑杰克");
//                                HttpRequestUtil.doGet(url);
//                                for (Map<String, String> player : playerList) {
//                                    if (player.get("baoxian") != null && "1".equals(player.get("baoxian"))) {
//                                        //判断玩家是否有黑杰克
//                                        if (checkPlayerBlackJack(player.get("poker"))) {
//                                            //玩家有黑杰克
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】有黑杰克,平手拿回押注金额，并赢得保险的1倍押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                            //玩家拿回押注金额
//                                            int money = Integer.parseInt(player.get("money"));
//                                            int yazhu = Integer.parseInt(player.get("yazhu"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                            money = Integer.parseInt(player.get("money"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                        } else {
//                                            //玩家没有黑杰克,赢得1倍押注金额
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】没有黑杰克,输掉押注金额，赢得保险的1倍押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                            int money = Integer.parseInt(player.get("money"));
//                                            int yazhu = Integer.parseInt(player.get("yazhu"));
//                                            player.put("money", String.valueOf(money + yazhu));
//
//                                        }
//                                    } else {
//                                        //玩家没有买保险
//                                        if (checkPlayerBlackJack(player.get("poker"))) {
//                                            //玩家有黑杰克
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】有黑杰克,平手拿回押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                            //玩家拿回押注金额
//                                            int money = Integer.parseInt(player.get("money"));
//                                            int yazhu = Integer.parseInt(player.get("yazhu"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                        } else {
//                                            //玩家没有黑杰克,赢得1倍押注金额
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】没有黑杰克,输掉押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                        }
//                                    }
//                                }
//                                //本局结束
//                                gamestep = END;
//                                lun++;
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player : playerList) {
//                                    sb.append("【" + player.get("name") + "】筹码为：" + player.get("money") + "\n");
//                                }
//                                url = BotUtil.getUrl(messagetype, code, "本轮结束，当前各玩家的筹码数为：" + "\n" + sb.toString());
//                                HttpRequestUtil.doGet(url);
//                                for (Map<String, String> player1 : playerList) {
//                                    player1.remove("yazhu");
//                                    player1.remove("baopai");
//                                    player1.remove("stop");
//                                    player1.put("poker", "");
//                                }
//                                checkGameEnd(messagetype, code);
//                            } else {
//                                //庄家没有黑杰克
//                                url = BotUtil.getUrl(messagetype, code, "庄家没有黑杰克，继续游戏");
//                                HttpRequestUtil.doGet(url);
//                                gamestep = SECONDDEAL;
//                                url = BotUtil.getUrl(messagetype, code, "进入询问环节 \n玩家可以输入【/21 要牌】 选择是否要牌 \n输入【/21 双倍押注】 是否要双倍押注  \n输入【/21 停牌】 是否要停牌");
//                                HttpRequestUtil.doGet(url);
//                            }
//
//
//                        }
//
//
//                    }
//                    //第二次发牌给玩家发一张牌
//                    if (inputmessage.trim().startsWith("要牌") && gamestep == SECONDDEAL) {
//                        String name = card;
//
//                        //玩家要牌
//                        for (Map<String, String> player : playerList) {
//                            if (name.equals(player.get("name"))) {
//                                //判断是否已经停牌或爆牌
//                                if ("1".equals(player.get("baopai")) || "1".equals(player.get("stop"))) {
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + name + "】已经停牌或爆牌，不能要牌");
//                                    HttpRequestUtil.doGet(url);
//                                    return;
//                                }
//                            }
//
//                        }
//
//
//                        String nextpoker = plateList.pop();
//                        String url = BotUtil.getUrl(messagetype, code, "【" + name + "】要牌，发牌：" + nextpoker);
//                        HttpRequestUtil.doGet(url);
//                        for (Map<String, String> player : playerList) {
//                            if (name.equals(player.get("name"))) {
//                                String poker = player.get("poker");
//                                poker = poker + " " + nextpoker;
//                                player.put("poker", poker);
//                                url = BotUtil.getUrl(messagetype, code, "【" + name + "】当前牌为：" + poker);
//                                HttpRequestUtil.doGet(url);
//                                break;
//                            }
//                        }
//                        //判断玩家是否爆牌
//                        if (checkPlayerBaoPai(name)) {
//                            //玩家爆牌
//                            for (Map<String, String> player : playerList) {
//                                if (name.equals(player.get("name"))) {
//                                    player.put("baopai", "1");
//                                    break;
//                                }
//                            }
//                            url = BotUtil.getUrl(messagetype, code, "【" + name + "】爆牌，输掉押注金额");
//                            HttpRequestUtil.doGet(url);
//                            //判断是否所有玩家都爆牌
//                            boolean allbaopai = true;
//                            for (Map<String, String> player1 : playerList) {
//                                if ("0".equals(player1.get("baopai")) || player1.get("baopai") == null) {
//                                    allbaopai = false;
//                                    break;
//                                }
//                            }
//                            if (allbaopai) {
//                                //所有玩家都爆牌
//                                gamestep = END;
//                                lun++;
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player1 : playerList) {
//                                    sb.append("【" + player1.get("name") + "】筹码为：" + player1.get("money") + "\n");
//                                }
//                                url = BotUtil.getUrl(messagetype, code, "本轮结束，当前各玩家的筹码数为：" + "\n" + sb.toString());
//                                for (Map<String, String> player1 : playerList) {
//                                    player1.remove("yazhu");
//                                    player1.remove("baopai");
//                                    player1.remove("stop");
//                                    player1.put("poker", "");
//                                }
//                                HttpRequestUtil.doGet(url);
//                                checkGameEnd(messagetype, code);
//                            }
//
//                        }
//
//                    }
//                    //玩家双倍押注
//                    if (inputmessage.trim().startsWith("双倍押注") && gamestep == SECONDDEAL) {
//                        //玩家双倍押注，并要牌
//                        String name = card;
//                        for (Map<String, String> player : playerList) {
//                            if (name.equals(player.get("name"))) {
//                                //判断是否已经停牌或爆牌
//                                if ("1".equals(player.get("baopai")) || "1".equals(player.get("stop"))) {
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + name + "】已经停牌或爆牌，不能双倍押注");
//                                    HttpRequestUtil.doGet(url);
//                                    return;
//                                }
//
//
//                                int money = Integer.parseInt(player.get("money"));
//                                int yazhu = Integer.parseInt(player.get("yazhu"));
//                                if (money >= yazhu) {
//                                    player.put("money", String.valueOf(money - yazhu));
//                                    player.put("yazhu", String.valueOf(yazhu * 2));
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + name + "】双倍押注，押注金额翻倍");
//                                    HttpRequestUtil.doGet(url);
//                                    String nextpoker = plateList.pop();
//                                    url = BotUtil.getUrl(messagetype, code, "【" + name + "】要牌，发牌：" + nextpoker);
//                                    HttpRequestUtil.doGet(url);
//                                    String poker = player.get("poker");
//                                    poker = poker + " " + nextpoker;
//                                    player.put("poker", poker);
//                                    url = BotUtil.getUrl(messagetype, code, "【" + name + "】当前牌为：" + poker);
//                                    HttpRequestUtil.doGet(url);
//                                    //判断玩家是否爆牌
//                                    if (checkPlayerBaoPai(name)) {
//                                        //玩家爆牌
//                                        player.put("baopai", "1");
//                                        url = BotUtil.getUrl(messagetype, code, "【" + name + "】爆牌，输掉押注金额");
//                                        HttpRequestUtil.doGet(url);
//                                        //判断是否所有玩家都爆牌
//                                        boolean allbaopai = true;
//                                        for (Map<String, String> player1 : playerList) {
//                                            if ("0".equals(player1.get("baopai")) || player1.get("baopai") == null) {
//                                                allbaopai = false;
//                                                break;
//                                            }
//                                        }
//                                        if (allbaopai) {
//                                            //所有玩家都爆牌
//                                            gamestep = END;
//                                            lun++;
//                                            StringBuilder sb = new StringBuilder();
//                                            for (Map<String, String> player1 : playerList) {
//                                                sb.append("【" + player1.get("name") + "】筹码为：" + player1.get("money") + "\n");
//                                            }
//                                            url = BotUtil.getUrl(messagetype, code, "本轮结束，当前各玩家的筹码数为：" + "\n" + sb.toString());
//                                            for (Map<String, String> player1 : playerList) {
//                                                player1.remove("yazhu");
//                                                player1.remove("baopai");
//                                                player1.remove("stop");
//                                                player1.put("poker", "");
//                                            }
//                                            HttpRequestUtil.doGet(url);
//                                            checkGameEnd(messagetype, code);
//                                        }
//
//                                    }
//
//
//                                } else {
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + name + "】双倍押注，押注金额不足，无法双倍押注");
//                                    HttpRequestUtil.doGet(url);
//                                }
//                                break;
//                            }
//                        }
//                    }
//
//
//                    //玩家停牌
//                    if (inputmessage.trim().startsWith("停牌") && gamestep == SECONDDEAL) {
//                        //玩家停牌
//                        String name = card;
//                        for (Map<String, String> player : playerList) {
//                            if (name.equals(player.get("name"))) {
//                                player.put("stop", "1");
//                                break;
//                            }
//                        }
//                        //判断是否所有玩家都停牌或爆牌
//                        boolean allplayerend = false;
//                        for (Map<String, String> player : playerList) {
//                            String stop = player.get("stop");
//                            String baopai = player.get("baopai");
//
//                            if ("1".equals(stop) || "1".equals(baopai)) {
//                                allplayerend = true;
//                            } else {
//                                allplayerend = false;
//                                break;
//                            }
//
//                            log.info("player "+player.get("name")+" stop:" + player.get("stop"));
//                            log.info("player"+player.get("name")+" baopai:" + player.get("baopai"));
//
//
//                        }
//                        if (allplayerend) {
//                            //所有玩家都停牌
//                            String url = BotUtil.getUrl(messagetype, code, "所有玩家都停牌，进入庄家发牌环节");
//                            HttpRequestUtil.doGet(url);
//                            gamestep = DEAL;
//                            //庄家发牌
//                            //如果庄家的牌小于17，庄家必须要牌
//                            url = BotUtil.getUrl(messagetype, code, "庄家牌为" + zhuangjiamingpoker.trim() + " " + zhuangjiaanpoker.trim());
//                            HttpRequestUtil.doGet(url);
//                            while (getPokerSum(zhuangjiamingpoker.trim() + " " + zhuangjiaanpoker.trim()) < 17) {
//                                String nextpoker = plateList.pop();
//                                url = BotUtil.getUrl(messagetype, code, "庄家要牌，发牌：" + nextpoker);
//                                HttpRequestUtil.doGet(url);
//                                zhuangjiamingpoker += " " + nextpoker;
//                                url = BotUtil.getUrl(messagetype, code, "庄家牌为" + zhuangjiamingpoker.trim() + " " + zhuangjiaanpoker.trim());
//                                HttpRequestUtil.doGet(url);
//                            }
//
//                            //判断庄家是否爆牌
//                            if (checkDealerBaoPai()) {
//                                //庄家爆牌
//                                url = BotUtil.getUrl(messagetype, code, "庄家爆牌，所有玩家赢得1倍押注金额");
//                                HttpRequestUtil.doGet(url);
//                                for (Map<String, String> player : playerList) {
//                                    if (player.get("baopai") == null || "0".equals(player.get("baopai"))) {
//                                        int money = Integer.parseInt(player.get("money"));
//                                        int yazhu = Integer.parseInt(player.get("yazhu"));
//                                        player.put("money", String.valueOf(money + yazhu));
//                                        money = Integer.parseInt(player.get("money"));
//                                        player.put("money", String.valueOf(money + yazhu));
//                                    }
//
//                                }
//
//                                gamestep = END;
//                                lun++;
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player1 : playerList) {
//                                    sb.append("【" + player1.get("name") + "】筹码为：" + player1.get("money") + "\n");
//                                }
//                                url = BotUtil.getUrl(messagetype, code, "本轮结束，当前各玩家的筹码数为：" + "\n" + sb.toString());
//                                HttpRequestUtil.doGet(url);
//                                for (Map<String, String> player1 : playerList) {
//                                    player1.remove("yazhu");
//                                    player1.remove("baopai");
//                                    player1.remove("stop");
//                                    player1.put("poker", "");
//                                }
//                                checkGameEnd(messagetype, code);
//
//                            } else {
//                                //庄家没有爆牌
//                                //判断庄家和玩家的牌的大小
//                                int zhuangjiapai = getPokerSum(zhuangjiamingpoker.trim() + " " + zhuangjiaanpoker.trim());
//                                for (Map<String, String> player : playerList) {
//                                    if (player.get("baopai") == null || "0".equals(player.get("baopai"))) {
//                                        int playerpai = getPokerSum(player.get("poker"));
//                                        if (playerpai > zhuangjiapai) {
//                                            //玩家赢
//                                            int money = Integer.parseInt(player.get("money"));
//                                            int yazhu = Integer.parseInt(player.get("yazhu"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                            money = Integer.parseInt(player.get("money"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】赢，赢得1倍押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                        } else if (playerpai == zhuangjiapai) {
//                                            //平局
//                                            int money = Integer.parseInt(player.get("money"));
//                                            int yazhu = Integer.parseInt(player.get("yazhu"));
//                                            player.put("money", String.valueOf(money + yazhu));
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】平手，取回押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                        } else {
//                                            //庄家赢
//                                            url = BotUtil.getUrl(messagetype, code, "【" + player.get("name") + "】输，输掉押注金额");
//                                            HttpRequestUtil.doGet(url);
//                                        }
//                                    }
//
//                                }
//                                gamestep = END;
//                                lun++;
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player1 : playerList) {
//                                    sb.append("【" + player1.get("name") + "】筹码为：" + player1.get("money") + "\n");
//                                }
//                                url = BotUtil.getUrl(messagetype, code, "本轮结束，当前各玩家的筹码数为：" + "\n" + sb.toString());
//                                HttpRequestUtil.doGet(url);
//                                for (Map<String, String> player1 : playerList) {
//                                    player1.remove("yazhu");
//                                    player1.remove("baopai");
//                                    player1.remove("stop");
//                                    player1.put("poker", "");
//                                }
//                                checkGameEnd(messagetype, code);
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void chaizhadan(JSONObject jsonParam) {
//        int messagetype = BotUtil.PRIVATE;
//        String code = "";
//        if ("group".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.QUN;
//            code = jsonParam.getString("group_id");
//        } else if ("private".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.PRIVATE;
//            code = jsonParam.getString("user_id");
//        } else {
//            return;
//        }
//
//        if ("message".equals(jsonParam.getString("post_type"))) {
//            String message = jsonParam.getString("message");
//            String card = jsonParam.getJSONObject("sender").getString("card");
//            if (card == null || "".equals(card)) {
//                card = jsonParam.getJSONObject("sender").getString("nickname");
//            }
//
//
//            if ("开始玩拆炸弹".equals(message)) {
//                gamestart = true;
//                //得到100以内随机整数
//                gamestep = JOIN;
//                zhadan = (int) (Math.random() * 100);
//            }
//            if ("结束玩拆炸弹".equals(message)) {
//                gamestart = false;
//                playerList.clear();
//                CurrentPlayerIndex=0;
//                minnum = 1;
//                maxnum = 100;
//                zhadan = 0;
//            }
//
//            if (gamestart) {
//                if (message.startsWith("/拆炸弹")) {
//                    String inputmessage = message.replace("/拆炸弹", "");
//                    if ("参加".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        Map<String, String> player = new HashMap<>();
//                        player.put("name", card);
//                        playerList.add(player);
//                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】已加入游戏");
//                        HttpRequestUtil.doGet(url);
//                    }
//
//                    if ("结束报名".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        String url = BotUtil.getUrl(messagetype, code, "报名结束，目前参与人数：" + playerList.size() + "人");
//                        HttpRequestUtil.doGet(url);
//                        CurrentPlayer = nextPlayer(playerList);
//                        url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "猜炸弹的数字");
//                        HttpRequestUtil.doGet(url);
//                        gamestep = ZHADANSTART;
//                    }
//                    if (card.equals(CurrentPlayer) && gamestep == ZHADANSTART && !"结束报名".equals(inputmessage.trim())) {
//                        String trim = inputmessage.trim();
//                        //判断输入的是否是数字
//                        if (trim.matches("[0-9]+")) {
//                            int input = Integer.parseInt(trim);
//                            if (input > 100) {
//                                String url = BotUtil.getUrl(messagetype, code, "输入的数字不能大于100");
//                                HttpRequestUtil.doGet(url);
//                            } else {
//                                if (input>=maxnum || input<=minnum) {
//                                    String url = BotUtil.getUrl(messagetype, code, "输入的数字必须在" + minnum + "和" + maxnum + "之间");
//                                    HttpRequestUtil.doGet(url);
//                                    return;
//                                }
//                                if (input == zhadan) {
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + card + "】被炸飞了，炸弹是：" + zhadan);
//                                    HttpRequestUtil.doGet(url);
//                                    url = BotUtil.getUrl(messagetype, code, "游戏结束");
//                                    HttpRequestUtil.doGet(url);
//                                    gamestart = false;
//                                    playerList.clear();
//                                    zhadan = 0;
//                                    minnum = 1;
//                                    maxnum = 100;
//                                } else {
//                                    if (input > zhadan) {
//                                        maxnum = input;
//                                    } else {
//                                        minnum = input;
//                                    }
//                                    String url = BotUtil.getUrl(messagetype, code, "【" + card + "】活了下来，炸弹在"+minnum+"-" + maxnum + "之间");
//                                    HttpRequestUtil.doGet(url);
//                                    CurrentPlayer = nextPlayer(playerList);
//                                    url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "猜炸弹的数字");
//                                    HttpRequestUtil.doGet(url);
//                                }
//                            }
//                        } else {
//                            String url = BotUtil.getUrl(messagetype, code, "输入的不是数字");
//                            HttpRequestUtil.doGet(url);
//                        }
//                    }
//                }
//
//            }
//        }
//
//
//    }
//
//    @Override
//    public void whoiswodi(JSONObject jsonParam) {
//        int messagetype = BotUtil.PRIVATE;
//        String code = "";
//        String groupid = "";
//        String userid = "";
//        if ("group".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.QUN;
//            code = jsonParam.getString("group_id");
//            groupid = jsonParam.getString("group_id");
//            userid = jsonParam.getString("user_id");
//        } else if ("private".equals(jsonParam.getString("message_type"))) {
//            messagetype = BotUtil.PRIVATE;
//            code = jsonParam.getString("user_id");
//        } else {
//            return;
//        }
//
//        if ("message".equals(jsonParam.getString("post_type"))) {
//            String message = jsonParam.getString("message");
//            String card = jsonParam.getJSONObject("sender").getString("card");
//            if (card == null || "".equals(card)) {
//                card = jsonParam.getJSONObject("sender").getString("nickname");
//            }
//
//
//            if ("开始玩谁是卧底".equals(message)) {
//                gamestart = true;
//                //得到100以内随机整数
//                gamestep = JOIN;
//            }
//            if ("结束玩谁是卧底".equals(message)) {
//                gamestart = false;
//                playerList.clear();
//                CurrentPlayerIndex = 0;
//            }
//
//            if (gamestart) {
//                if (message.startsWith("/卧底")) {
//                    String inputmessage = message.replace("/卧底", "");
//                    if ("参加".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        Map<String, String> player = new HashMap<>();
//                        player.put("name", card);
//                        player.put("userid", userid);
//                        playerList.add(player);
//                        String url = BotUtil.getUrl(messagetype, code, "【" + card + "】已加入游戏");
//                        HttpRequestUtil.doGet(url);
//                    }
//
//                    if ("结束报名".equals(inputmessage.trim()) && gamestep == JOIN) {
//                        //报名人数要大于3人
//                        if (playerList.size() < 3) {
//                            String url = BotUtil.getUrl(messagetype, code, "报名人数不能少于3人");
//                            HttpRequestUtil.doGet(url);
//                            return;
//                        }
//
//                        String url = BotUtil.getUrl(messagetype, code, "报名结束，目前参与人数：" + playerList.size() + "人");
//                        HttpRequestUtil.doGet(url);
//
//
//
//                        Integer integer = gameWodiMapper.selectCount(null);
//                        if (integer == null || integer == 0) {
//                            String url1 = BotUtil.getUrl(messagetype, code, "没有词库");
//                            HttpRequestUtil.doGet(url1);
//                            return;
//                        }
//                        //随机取一个词库
//                        int i = (int) (Math.random() * integer)+1;
//                        GameWodi gameWodi = gameWodiMapper.selectById(i);
//                        String wodi = gameWodi.getWodiWork();
//                        String pingminWord = gameWodi.getPingminWord();
//
//                        //随机取一个卧底
//                        int i1 = (int) (Math.random() * playerList.size());
//                        //给每个人分配词语
//                        for (int i2 = 0; i2 < playerList.size(); i2++) {
//                            Map<String, String> player = playerList.get(i2);
//                            if (i2 == i1) {
//                                player.put("word", wodi);
//                                player.put("role", "卧底");
//                                player.put("vote", "0");
//                                //私聊词语
//                                String url2 = BotUtil.sendQunPrivate(player.get("userid"), groupid, "你是卧底，卧底词是：" + wodi);
//                                HttpRequestUtil.doGet(url2);
//                            } else {
//                                player.put("word", pingminWord);
//                                player.put("role", "平民");
//                                player.put("vote", "0");
//
//                                //私聊词语
//                                String url2 = BotUtil.sendQunPrivate(player.get("userid"), groupid, "你是平民，平民词是：" + pingminWord);
//                                HttpRequestUtil.doGet(url2);
//                            }
//
//                        }
//                        String url2 = BotUtil.getUrl(messagetype, code, "卧底已选出，每人的词语已私聊,查看后游戏开始。");
//                        HttpRequestUtil.doGet(url2);
//                        CurrentPlayer = nextPlayer(playerList);
//                        url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "描述自己的词语");
//                        HttpRequestUtil.doGet(url);
//                        gamestep = MIAOSHU;
//                    }
//                    if (card.equals(CurrentPlayer) && (gamestep == MIAOSHU || gamestep == PINGPIAOMIAOSHU) && !"结束报名".equals(inputmessage.trim())) {
//
//                        List<Map<String, String>> list = this.playerList;
//                        if(gamestep == MIAOSHU){
//                            list = this.playerList;
//                        }else {
//                            list = this.pingpiaoplayerList;
//                        }
//
//                        CurrentPlayer = nextPlayer(list);
//                        //找到当前玩家，修改为已描述
//                        for (int i = 0; i < list.size(); i++) {
//                            Map<String, String> player = list.get(i);
//                            if (player.get("name").equals(card)) {
//                                player.put("miaoshu", "1");
//                            }
//                        }
//
//                        String url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "描述自己的词语");
//                        HttpRequestUtil.doGet(url);
//
//                        //判断是否所有玩家都描述完了
//                        boolean isAllMiaoshu = true;
//                        //判断是否所有玩家都描述完了
//                        for (int i = 0; i < list.size(); i++) {
//                            Map<String, String> player = list.get(i);
//                            if (player.get("miaoshu") == null) {
//                                isAllMiaoshu = false;
//                                break;
//                            }
//                        }
//                        if (isAllMiaoshu) {
//
//                            CurrentPlayerIndex = 0;
//                            CurrentPlayer = nextPlayer(playerList);
//                            url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "投票");
//                            HttpRequestUtil.doGet(url);
//                            if(gamestep == MIAOSHU){
//                                gamestep = VOTE;
//                            }else {
//                                gamestep = PINGPIAOVOTE;
//                            }
//                            //清空描述状态
//                            for (int i = 0; i < list.size(); i++) {
//                                Map<String, String> player = list.get(i);
//                                player.put("miaoshu", null);
//                            }
//                        }
//
//
//
//
//
//                    }
//
//                    if (card.equals(CurrentPlayer) && (gamestep == VOTE||gamestep == PINGPIAOVOTE) && !"结束报名".equals(inputmessage.trim())) {
//                        //投票
//                        boolean isVote = false;
//                        List<Map<String, String>> list ;
//                        if (gamestep == VOTE) {
//                            list = playerList;
//                        } else {
//                            list = pingpiaoplayerList;
//                        }
//
//                        for (Map<String, String> player : list) {
//                            if (player.get("name").equals(inputmessage.trim())) {
//                                //判断是否投给自己
//                                if (player.get("name").equals(card)) {
//                                    String url = BotUtil.getUrl(messagetype, code, "不能投给自己");
//                                    HttpRequestUtil.doGet(url);
//                                    return;
//                                }
//                                isVote = true;
//                                Integer vote = Integer.valueOf(player.get("vote"));
//                                vote++;
//                                player.put("vote", vote.toString());
//                            }
//                        }
//                        if (!isVote) {
//                            String url = BotUtil.getUrl(messagetype, code, "不能投给"+inputmessage.trim()+"，请重新投票");
//                            HttpRequestUtil.doGet(url);
//                            return;
//                        }else {
//                            //修改玩家投票状态
//                            for (Map<String, String> player : list) {
//                                if (player.get("name").equals(card)) {
//                                    player.put("isvote", "1");
//                                }
//                            }
//                        }
//
//                        //判断是否所有玩家都投票完了
//
//                        boolean isAllVote = true;
//                        for (Map<String, String> player : playerList) {
//                            if (player.get("isvote") == null) {
//                                isAllVote = false;
//                                break;
//                            }
//                        }
//                        if (isAllVote) {
//                            //判断是否有平票
//                            Integer maxVote = 0;
//                            for (Map<String, String> player : list) {
//                                Integer vote = Integer.valueOf(player.get("vote"));
//                                if (vote > maxVote) {
//                                    maxVote = vote;
//                                }
//                            }
//                            for (Map<String, String> player : list) {
//                                Integer vote = Integer.valueOf(player.get("vote"));
//                                if (vote == maxVote) {
//                                    if (gamestep == VOTE){
//                                        //没有同名玩家就加入pingpiaoplayerList
//                                        boolean isExist = false;
//                                        for (Map<String, String> pingpiaoplayer : pingpiaoplayerList) {
//                                            if (pingpiaoplayer.get("name").equals(player.get("name"))) {
//                                                isExist = true;
//                                                break;
//                                            }
//                                        }
//                                        if (!isExist) {
//                                            player.put("maxagane","1");
//                                            pingpiaoplayerList.add(player);
//                                        }
//                                    }
//                                }else {
//                                    if (gamestep==PINGPIAOVOTE){
//                                        //找到玩家修改maxagane为0
//                                        for (Map<String, String> player1 : playerList) {
//                                            if (player1.get("name").equals(player.get("name"))){
//                                                player1.put("maxagane","0");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            //删除pingpiaoplayerList中maxagane为0的玩家
//                            for (int i = 0; i < pingpiaoplayerList.size(); i++) {
//                                Map<String, String> player = pingpiaoplayerList.get(i);
//                                if ("0".equals(player.get("maxagane"))) {
//                                    pingpiaoplayerList.remove(i);
//                                    i--;
//                                }
//                            }
//
//                            if (pingpiaoplayerList.size() > 1) {
//                                //获取每个玩家的票数
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player : list) {
//                                    sb.append(player.get("name") + "：" + player.get("vote") + "票\n");
//                                }
//                                //通知玩家
//                                String url = BotUtil.getUrl(messagetype, code, "投票结束，玩家票数如下：\n" + sb.toString());
//                                HttpRequestUtil.doGet(url);
//                                //清空玩家的投票状态
//                                for (Map<String, String> player : playerList) {
//                                    player.put("isvote", null);
//                                    player.put("vote", "0");
//                                }
//                                for (Map<String, String> player : pingpiaoplayerList) {
//                                    player.put("isvote", null);
//                                    player.put("vote", "0");
//                                }
//                                //平票的人再次描述
//                                CurrentPlayerIndex = 0;
//                                CurrentPlayer = nextPlayer(pingpiaoplayerList);
//                                //平票玩家名称
//                                String pingpiaoname = "";
//                                for (Map<String, String> player : pingpiaoplayerList) {
//                                    pingpiaoname += player.get("name") + " ";
//                                }
//
//                                url = BotUtil.getUrl(messagetype, code, "玩家" + pingpiaoname + "平票，再次描述");
//                                HttpRequestUtil.doGet(url);
//
//                                url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "描述自己的词语");
//                                HttpRequestUtil.doGet(url);
//                                gamestep = PINGPIAOMIAOSHU;
//
//
//
//                            }
//
//                            if (pingpiaoplayerList.size() == 1) {
//                                //获取每个玩家的票数
//                                StringBuilder sb = new StringBuilder();
//                                for (Map<String, String> player : list) {
//                                    sb.append(player.get("name") + "：" + player.get("vote") + "票\n");
//                                }
//                                //通知玩家
//                                String url = BotUtil.getUrl(messagetype, code, "投票结束，玩家票数如下：\n" + sb.toString());
//                                HttpRequestUtil.doGet(url);
//                                //清空玩家的投票状态
//                                for (Map<String, String> player : playerList) {
//                                    player.put("isvote", null);
//                                    player.put("vote", "0");
//                                }
//                                url = BotUtil.getUrl(messagetype, code, "【" +pingpiaoplayerList.get(0).get("name")+"】被票出");
//                                HttpRequestUtil.doGet(url);
//
//
//                                //判断是否有卧底
//                                boolean isWodi = false;
//                                if (pingpiaoplayerList.get(0).get("role").equals("卧底")) {
//                                    isWodi = true;
//                                }
//                                if (isWodi) {
//                                    //有卧底
//                                    url = BotUtil.getUrl(messagetype, code, "游戏结束，【" + pingpiaoplayerList.get(0).get("name") + "】是卧底，卧底词语是" + pingpiaoplayerList.get(0).get("word"));
//                                    HttpRequestUtil.doGet(url);
//                                    playerList.clear();
//                                    CurrentPlayerIndex = 0;
//                                    pingpiaoplayerList.clear();
//                                    gamestep = END;
//                                } else {
//                                    //没有卧底,得票最高的人出局
//                                    url = BotUtil.getUrl(messagetype, code, "【" + pingpiaoplayerList.get(0).get("name") + "】是平民，出局");
//                                    HttpRequestUtil.doGet(url);
//                                    this.playerList.remove(pingpiaoplayerList.get(0));
//                                    //判断玩家数量是否大于2，如果小于等于2，游戏结束，卧底胜利
//                                    if (this.playerList.size() <= 2) {
//                                        //获取卧底玩家
//                                        for (Map<String, String> player : this.playerList) {
//                                            if (player.get("role").equals("卧底")) {
//                                                url = BotUtil.getUrl(messagetype, code, "游戏结束，卧底胜利,【" + player.get("name") + "】是卧底，卧底词语是" + player.get("word"));
//                                                HttpRequestUtil.doGet(url);
//                                                playerList.clear();
//                                                CurrentPlayerIndex = 0;
//                                                pingpiaoplayerList.clear();
//                                                gamestep = END;
//                                            }
//                                        }
//                                    } else {
//                                        //游戏继续
//                                        CurrentPlayerIndex = 0;
//                                        CurrentPlayer = nextPlayer(this.playerList);
//                                        url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "描述自己的词语");
//                                        HttpRequestUtil.doGet(url);
//                                        gamestep = MIAOSHU;
//                                        //清空平票列表
//                                        pingpiaoplayerList.clear();
//                                        //清理投票
//                                        for (Map<String, String> player : this.playerList) {
//                                            player.put("vote", "0");
//                                        }
//                                    }
//                                }
//                            }
//
//                        }else {
//                            CurrentPlayer = nextPlayer(this.playerList);
//                            String url = BotUtil.getUrl(messagetype, code, "请" + CurrentPlayer + "投票");
//                            HttpRequestUtil.doGet(url);
//                            gamestep = VOTE;
//                        }
//                    }
//                }
//
//            }
//        }
//    }

    public String nextPlayer(List<Map<String, String>> list) {
        if (list.size() > 0) {

            Map<String, String> stringStringMap = list.get(CurrentPlayerIndex);
            String name = stringStringMap.get("name");
            if (CurrentPlayerIndex == list.size() - 1) {
                CurrentPlayerIndex = 0;
            } else {
                CurrentPlayerIndex++;
            }
            return name;
        } else {
            return "";
        }
    }

    //判断总轮数是否大于5，如果大于5，查看当前玩家的筹码数，并排名
//    private void checkGameEnd(int messagetype, String code) {
//        if (lun > 5) {
//            //查看当前玩家的筹码数，并排名
//            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//            for (Map<String, String> player : playerList) {
//                list.add(player);
//            }
//            Collections.sort(list, new Comparator<Map<String, String>>() {
//                @Override
//                public int compare(Map<String, String> o1, Map<String, String> o2) {
//                    int money1 = Integer.parseInt(o1.get("money"));
//                    int money2 = Integer.parseInt(o2.get("money"));
//                    return money2 - money1;
//                }
//            });
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < list.size(); i++) {
//                Map<String, String> player = list.get(i);
//                sb.append("第" + (i + 1) + "名：" + player.get("name") + "，筹码数：" + player.get("money") + "\n");
//            }
//            String url = BotUtil.getUrl(messagetype, code, "游戏结束，排名如下：" + "\n" + sb.toString());
//            HttpRequestUtil.doGet(url);
//            gamestep = END;
//            playerList.clear();
//            zhuangjiamingpoker = "";
//            zhuangjiaanpoker = "";
//        } else {
//            gamestep = YAZHU;
//            String url = BotUtil.getUrl(messagetype, code, "开始第：" + lun + "局\n" + "请玩家下注");
//            HttpRequestUtil.doGet(url);
//            zhuangjiamingpoker = "";
//            zhuangjiaanpoker = "";
//        }
//
//    }


    /**
     * 判断庄家是否爆牌
     *
     * @return
     */
    private boolean checkDealerBaoPai() {
        int sum = getPokerSum(zhuangjiamingpoker.trim() + " " + zhuangjiaanpoker.trim());
        if (sum > 21) {
            return true;
        }
        return false;
    }

    public int getPokerSum(String pokerlist) {
        String[] pokers = pokerlist.split(" ");
        int sum = 0;
        for (String poker : pokers) {
            String[] poker1Arr = poker.split("-");
            if ("J".equals(poker1Arr[1]) || "Q".equals(poker1Arr[1]) || "K".equals(poker1Arr[1])) {
                sum += 10;
            } else if ("A".equals(poker1Arr[1])) {
                sum += 11;
            } else {
                sum += Integer.parseInt(poker1Arr[1]);
            }
            //如果有A，且总和大于21，A的值为1

        }
        if (sum > 21 && pokerlist.contains("A")) {
            sum -= 10;
        }
        return sum;
    }


    public boolean checkPlayerBaoPai(String name) {
        for (Map<String, String> player : playerList) {
            if (name.equals(player.get("name"))) {
                String poker = player.get("poker");
                String[] pokers = poker.split(" ");
                int sum = 0;
                for (String p : pokers) {
                    p = p.split("-")[1];

                    if (p.startsWith("A")) {
                        sum += 11;
                    } else if (p.startsWith("2")) {
                        sum += 2;
                    } else if (p.startsWith("3")) {
                        sum += 3;
                    } else if (p.startsWith("4")) {
                        sum += 4;
                    } else if (p.startsWith("5")) {
                        sum += 5;
                    } else if (p.startsWith("6")) {
                        sum += 6;
                    } else if (p.startsWith("7")) {
                        sum += 7;
                    } else if (p.startsWith("8")) {
                        sum += 8;
                    } else if (p.startsWith("9")) {
                        sum += 9;
                    } else if (p.startsWith("10") || p.startsWith("J") || p.startsWith("Q") || p.startsWith("K")) {
                        sum += 10;
                    }
                }
                if (sum > 21) {
                    //是否有A
                    boolean hasA = false;
                    for (String p : pokers) {
                        p = p.split("-")[1];
                        if (p.startsWith("A")) {
                            hasA = true;
                            break;
                        }
                    }
                    if (hasA) {
                        //有A
                        sum = sum - 10;
                        if (sum > 21) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public boolean checkPlayerBlackJack(String poker) {
        if (poker.contains("A") && (poker.contains("J") || poker.contains("Q") || poker.contains("K") || poker.contains("10"))) {
            return true;
        }
        return false;
    }

    public boolean checkBaoxian() {
        for (Map<String, String> player : playerList) {
            if (player.get("baoxian") == null) {
                return false;
            }
        }
        return true;
    }


    public boolean checkZhuangjiaBlackJack(String poker1) {
        if (poker1.contains("J") || poker1.contains("10") || poker1.contains("Q") || poker1.contains("K")) {
            return true;
        }
        return false;

    }

    public boolean checkYazhu() {
        boolean flag = true;
        for (Map<String, String> player : playerList) {
            if (player.get("yazhu") == null) {
                flag = false;
            }
        }

        if (flag) {
            //所有玩家都下注了 扣除玩家的钱
            for (Map<String, String> player : playerList) {
                int money = Integer.parseInt(player.get("money"));
                int yazhu = Integer.parseInt(player.get("yazhu"));
                player.put("money", String.valueOf(money - yazhu));
            }
        }
        return flag;
    }

    public void initPoker() {
        //初始化扑克牌
        String[] colors = {"♠", "♥", "♣", "♦"};
        String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (String number : numbers) {
            for (String color : colors) {
                plateList.add(color + "-" + number);
            }
        }
        //洗牌
        for (int i = 0; i < plateList.size(); i++) {
            int index = (int) (Math.random() * plateList.size());
            String temp = plateList.get(i);
            plateList.set(i, plateList.get(index));
            plateList.set(index, temp);
        }
    }

    public void dealFirstPoker(int messagetype, String Code) {
        //发牌
        for (int i = 0; i < playerList.size(); i++) {
            Map<String, String> player = playerList.get(i);
            String playername = player.get("name");
            String poker1 = plateList.pop();
            String poker2 = plateList.pop();
            player.put("poker", poker1 + " " + poker2);
            player.put("score", getScore(poker1, poker2));
//            String url = BotUtil.getUrl(messagetype, Code, "【" + playername + "】的牌为：" + poker1 + " " + poker2 + "，当前点数为：" + player.get("score"));
//            HttpRequestUtil.doGet(url);
        }
    }


    public String getScore(String poker1, String poker2) {
        String[] poker1Arr = poker1.split("-");
        String[] poker2Arr = poker2.split("-");
        int score = 0;
        if ("J".equals(poker1Arr[1]) || "Q".equals(poker1Arr[1]) || "K".equals(poker1Arr[1])) {
            score += 10;
        } else if ("A".equals(poker1Arr[1])) {
            score += 11;
        } else {
            score += Integer.parseInt(poker1Arr[1]);
        }
        if ("J".equals(poker2Arr[1]) || "Q".equals(poker2Arr[1]) || "K".equals(poker2Arr[1])) {
            score += 10;
        } else if ("A".equals(poker2Arr[1])) {
            score += 11;
            if (score > 21) {
                score -= 10;
            }
        } else {
            score += Integer.parseInt(poker2Arr[1]);
        }
        return score + "";
    }


    @Override
    public void guessNumber(BotMessage message) {
        String userInput = message.getRaw_message().trim().replace("/guess", "").trim();
        GuessNumberParam guessNumberParam = null;
        String messageType = message.getMessage_type();
        //群聊
        String groupId = message.getGroup_id()==null?"":message.getGroup_id();
        String userId = message.getUser_id();
        for (GameBaseParam guessNumberGameBaseParam : GameParam.gameList) {
            if (groupId.equals(guessNumberGameBaseParam.getGroupId()) && messageType.equals(guessNumberGameBaseParam.getRepType()) && "猜数字".equals(guessNumberGameBaseParam.getGameName())) {
                guessNumberParam = (GuessNumberParam) guessNumberGameBaseParam;
            }
            if (userId.equals(guessNumberGameBaseParam.getUserId()) && messageType.equals(guessNumberGameBaseParam.getRepType()) && "猜数字".equals(guessNumberGameBaseParam.getGameName())) {
                guessNumberParam = (GuessNumberParam) guessNumberGameBaseParam;
            }
        }
        if ("开始".equals(userInput)) {


            //判断是群聊还是私聊
            if (guessNumberParam == null) {
                guessNumberParam = new GuessNumberParam();
                guessNumberParam.setGroupId(groupId);
                guessNumberParam.setUserId(userId);
                guessNumberParam.setRepType(messageType);
                guessNumberParam.setPlayerList(new ArrayList<>());
                guessNumberParam.setGameName("猜数字");
                guessNumberParam.setGameStatus(GameBaseParam.START);
                guessNumberParam.setGameStep(GameBaseParam.JOIN);
                guessNumberParam.setAnswer(getfournorepeatedRandomNum());
                guessNumberParam.setGuessIndex("0");
                //如果是私聊，直接添加到玩家列表并开始游戏
                if (messageType.equals("private")) {
                    PlayerBaseParam player = new PlayerBaseParam();
                    player.setUserIndex(1);
                    player.setUserId(userId);
                    player.setUserName(message.getSender().getNickname());
                    guessNumberParam.getPlayerList().add(player);
                    guessNumberParam.setGameStep(GameBaseParam.GUESSNUMBER_GUESS);
                    guessNumberParam.setCurrentListenPlayer(player);
                    BotUtil.sendPrivate(message.getUser_id(), "我会给出四位不重复的数字, 每次猜测的数字格式为【/guess 1234】,\n" +
                            "根据猜测的数字我会给出xAyB的反馈,A代表数字正确位置也正确,B代表数字正确但位数不正确。举例：若答案为7596 猜测为4569 我会给出 1A2B的反馈,中途退出你可以发送【/guess 结束】来结束游戏。开始游戏吧~");
                }else {
                    BotUtil.sendGroup(message.getGroup_id(), "游戏开始了，发送【/guess 参加】来加入游戏");
                }
                GameParam.gameList.add(guessNumberParam);
            } else {
                //游戏已经开始了
                BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "游戏已经开始了");

            }

            return;

        }
        //群聊中需要加入游戏
        if (userInput.equals("参加") &&guessNumberParam.getGameStep() == GameBaseParam.JOIN && guessNumberParam.getRepType().equals("group")) {
            PlayerBaseParam player = new PlayerBaseParam();
            player.setUserIndex(guessNumberParam.getPlayerList().size() + 1);
            player.setUserId(userId);
            player.setUserName(StringUtil.isBlank(message.getSender().getCard()) ? message.getSender().getNickname() : message.getSender().getCard());
            guessNumberParam.getPlayerList().add(player);
            BotUtil.sendGroup(message.getGroup_id(), "【" + player.getUserName() + "】加入了游戏");
            return;
        }
        //结束报名
        if (userInput.equals("结束报名") && guessNumberParam.getGameStep() == GameBaseParam.JOIN && guessNumberParam.getRepType().equals("group")) {
            PlayerBaseParam player = guessNumberParam.getPlayerList().get(0);
            guessNumberParam.setCurrentListenPlayer(player); //设置当前轮到的玩家
            guessNumberParam.setGameStep(GameBaseParam.GUESSNUMBER_GUESS);
            //统计当前玩家数量
            int playerNum = guessNumberParam.getPlayerList().size();
            //通知玩家游戏数量
            BotUtil.sendGroup(message.getGroup_id(), "游戏开始，当前玩家数量为【" + playerNum + "】");
            BotUtil.sendGroup(message.getGroup_id(), "我会给出四位不重复的数字, 每次猜测的数字格式为【/guess 1234】,\n" +
                    "根据猜测的数字我会给出xAyB的反馈,A代表数字正确位置也正确,B代表数字正确但位数不正确。举例：若答案为7596 猜测为4569 我会给出 1A2B的反馈,中途退出你可以发送【/guess 结束】来结束游戏。开始游戏吧~");
            BotUtil.sendGroup(message.getGroup_id(), "请"+guessNumberParam.getCurrentListenPlayer().getUserIndex()+"号【" + guessNumberParam.getCurrentListenPlayer().getUserName() + "】开始猜数字");
            return;
        }
        //结束游戏
        if (userInput.equals("结束")) {
            BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "游戏结束，答案为【" + guessNumberParam.getAnswer() + "】");
            GameParam.gameList.remove(guessNumberParam);
            return;
        }
        //猜数字
        if (guessNumberParam.getGameStep() == GameBaseParam.GUESSNUMBER_GUESS && userId.equals(guessNumberParam.getCurrentListenPlayer().getUserId())) {
            //判断是否是4位不重复数字
            if (!isFourNoRepeatNum(userInput)) {
                BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "请输入四位不重复的数字");
                return;
            }
            int guessIndex = Integer.valueOf(guessNumberParam.getGuessIndex());
            guessNumberParam.setGuessIndex(String.valueOf(guessIndex+1));
            //判断是否猜对
            String repMessage = JudgeNumber(userInput, guessNumberParam.getAnswer());
            if ("4A0B".equals(repMessage)) {
                String sendUserName = "你";
                if (messageType.equals("group")) {
                    sendUserName = "【"+guessNumberParam.getCurrentListenPlayer().getUserName()+"】";
                }
                BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "恭喜"+sendUserName+"猜对了，一共猜了"+guessNumberParam.getGuessIndex()+"次，答案为【" + guessNumberParam.getAnswer() + "】");
                GameParam.gameList.remove(guessNumberParam);
                return;
            }else {
                BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "不对，"+repMessage);
                Integer userIndex = guessNumberParam.getCurrentListenPlayer().getUserIndex();
                if (userIndex == guessNumberParam.getPlayerList().size()) {
                    guessNumberParam.setCurrentListenPlayer(guessNumberParam.getPlayerList().get(0));
                }else {
                    guessNumberParam.setCurrentListenPlayer(guessNumberParam.getPlayerList().get(userIndex));
                }
                if (messageType.equals("group")) {
                    BotUtil.sendGroup(message.getGroup_id(), "请"+guessNumberParam.getCurrentListenPlayer().getUserIndex()+"号【" + guessNumberParam.getCurrentListenPlayer().getUserName() + "】猜数字");
                }
            }


        }


    }

    private boolean isFourNoRepeatNum(String userInput) {
        if (userInput.length() != 4) {
            return false;
        }
        for (int i = 0; i < userInput.length(); i++) {
            for (int j = i + 1; j < userInput.length(); j++) {
                if (userInput.charAt(i) == userInput.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }
    //1-9不重复的四位数
    private String getfournorepeatedRandomNum() {
        String answer = "";
        while (answer.length() < 4) {
            int num = (int) (Math.random() * 9 + 1);
            if (!answer.contains(String.valueOf(num))) {
                answer += num;
            }
        }
        return answer;
    }

    //判断数字与目标有几位相同
    public String JudgeNumber (String number,String code){
        int count = 0;
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == code.charAt(i)){
                count++;
            }
        }
        int count2 = 0;

        for (int i = 0; i < number.length(); i++) {
            if (code.contains(String.valueOf(number.charAt(i)))){
                count2++;
            }
        }

        return count+"A"+(count2-count)+"B";
    }

    @Override
    public void execute(BotMessage message, String cmd) {
        guessNumber(message);
    }
}