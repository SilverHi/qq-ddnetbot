package com.silver.ddrtools.bot.service.impl;

import com.silver.ddrtools.bot.service.BotService;
import com.silver.ddrtools.ddr.service.DDrService;
import com.silver.ddrtools.game.service.GuessNumberGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class BotServiceImpl implements BotService {


    List<Map<String,String>> gameuserlist = new ArrayList<>();

    String gamename = "";

    @Autowired
    public DDrService   dDrService;

    @Autowired
    public GuessNumberGameService gameService;

//    public void getMessage(HttpServletRequest request) {
//        //JSONObject
//        JSONObject jsonParam = BotUtil.getJSONParam(request);
//        log.info("接收参数为:{}",jsonParam.toString() !=null ? jsonParam.toString() : "FALSE");
//        int messagetype = BotUtil.PRIVATE;
//        String code = "";
//        if ("group".equals(jsonParam.getString("message_type"))){
//            messagetype = BotUtil.QUN;
//            code = jsonParam.getString("group_id");
//        }else if ("private".equals(jsonParam.getString("message_type"))){
//            messagetype = BotUtil.PRIVATE;
//            code = jsonParam.getString("user_id");
//        }else {
//            return;
//        }
//
//        if("message".equals(jsonParam.getString("post_type"))){
//            String message = jsonParam.getString("message");
//
//            if ("开始玩21点".equals(message)){
//                gamename =  "21点";
//                String url = BotUtil.getUrl(messagetype, code, "21点游戏开始了，输入【/21 参加】加入游戏");
//                HttpRequestUtil.doGet(url);
//
//            }
//
//
//            if (gamename!=null && !"".equals(gamename)){
//                if ("21点".equals(gamename)){
//                    gameService.play21(jsonParam);
//                }
//            }
//            if ("结束玩21点".equals(message)){
//                gamename =  "";
//                String url = BotUtil.getUrl(messagetype, code, "21点结束");
//                HttpRequestUtil.doGet(url);
//
//            }
//
//            if ("开始玩拆炸弹".equals(message)){
//                gamename =  "拆炸弹";
//                String url = BotUtil.getUrl(messagetype, code, "拆炸弹游戏开始了，输入【/拆炸弹 参加】加入游戏");
//                HttpRequestUtil.doGet(url);
//
//            }
//            if (gamename!=null && !"".equals(gamename)){
//                if ("拆炸弹".equals(gamename)){
//                    gameService.chaizhadan(jsonParam);
//                }
//            }
//            if ("结束玩拆炸弹".equals(message)){
//                gamename =  "";
//                String url = BotUtil.getUrl(messagetype, code, "拆炸弹结束");
//                HttpRequestUtil.doGet(url);
//
//            }
//
//            if ("开始玩谁是卧底".equals(message)){
//                gamename =  "谁是卧底";
//                String url = BotUtil.getUrl(messagetype, code, "谁是卧底游戏开始了，输入【/卧底 参加】加入游戏");
//                HttpRequestUtil.doGet(url);
//
//            }
//            if (gamename!=null && !"".equals(gamename)){
//                if ("谁是卧底".equals(gamename)){
//                    gameService.whoiswodi(jsonParam);
//                }
//            }
//            if ("结束玩谁是卧底".equals(message)){
//                gamename =  "";
//                String url = BotUtil.getUrl(messagetype, code, "谁是卧底结束");
//                HttpRequestUtil.doGet(url);
//
//            }
//
//
//
//
//            //猜数字
//            String finalCode = code;
//
//            if("开始玩猜数字".equals(message)) {
//                String url = BotUtil.getUrl(messagetype, code, "猜数字游戏开始, 我会给出四位不重复的数字,每次猜测需要发送一条消息, 每次猜测的数字格式为: 1234, " +
//                        "根据猜测的数字我会给出xAyB的反馈,A代表数字正确位置也正确,B代表数字正确但位数不正确。举例：若答案为7596 猜测为4569 我会给出 1A2B的反馈,中途退出你可以发送'结束猜数字'来结束游戏。开始游戏吧~");
//                HttpRequestUtil.doGet(url);
//                Map<String,String> userdata = new HashMap<>();
//                userdata.put("code",code);
//                userdata.put("num",getfournorepeatedRandomNum());
//                userdata.put("count",0+"");
//                gameuserlist.add(userdata);
//            }
//            if("结束玩猜数字".equals(message)) {
//                String url = BotUtil.getUrl(messagetype, code, "猜数字游戏结束");
//                HttpRequestUtil.doGet(url);
//                Map<String, String> code1 = gameuserlist.stream().filter(map -> String.valueOf(map.get("code")).equals(finalCode)).findAny().orElse(null);
//                gameuserlist.remove(code1);
//            }
//            Map<String, String> code1 = gameuserlist.stream().filter(map -> String.valueOf(map.get("code")).equals(finalCode)).findAny().orElse(null);
//            if (code1 != null) {
//                //校验message
//                message = message.trim();
//                if (message.matches("[0-9]+")&& message.length() == 4) {
//                    String judgeresult = JudgeNumber(message, code1.get("num"));
//                    if ("4A0B".equals(judgeresult)) {
//                        String url = BotUtil.getUrl(messagetype, code, "猜对了,答案是" + code1.get("num") + ",一共猜了" + code1.get("count") + "次");
//                        HttpRequestUtil.doGet(url);
//                        gameuserlist.remove(code1);
//                    }  else {
//                        String url = BotUtil.getUrl(messagetype, code, judgeresult);
//                        code1.put("count",Integer.parseInt(code1.get("count"))+1+"");
//                        HttpRequestUtil.doGet(url);
//                    }
//                } else {
//                    String url = BotUtil.getUrl(messagetype, code, "请输入四位不重复的数字");
//                    HttpRequestUtil.doGet(url);
//                }
//                return;
//            }
//
//
//
//            if (message.startsWith("points") || message.startsWith("Points")) {
//                String name = message.replace("points","");
//                int finalMessagetype = messagetype;
//                String finalCode1 = code;
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dDrService.queryPoints(finalMessagetype, finalCode1,formatName(name.trim()));
//                    }
//                });
//                thread.run();
//
//            }
//            if (message.startsWith("未完成图") || message.startsWith("未完成图")) {
//                String name = message.replace("未完成图","");
//                String[] s = name.trim().split(" ");
//                if (s.length>1){
//                    int finalMessagetype1 = messagetype;
//                    String finalCode2 = code;
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dDrService.queryUnFinishedMap(finalMessagetype1, finalCode2,formatName(s[1]),s[0]);
//                        }
//                    });
//                    thread.run();
//
//                }else {
//                    String message2= "请输入正确的格式：未完成图 {地图类型} {id}\n" +
//                            "地图类型可选参数为：\n" +
//                            "简单 \n" +
//                            "中阶 \n" +
//                            "高阶 \n" +
//                            "疯狂 \n" +
//                            "分身 \n" +
//                            "古典简单 \n" +
//                            "古典中阶 \n" +
//                            "古典高阶 \n" +
//                            "古典疯狂 \n" +
//                            "传统 \n" +
//                            "solo \n" +
//                            "传统 \n" +
//                            "竞速 \n" +
//                            "娱乐 \n" ;
//                    String url1 = BotUtil.getUrl(messagetype,code,message2);
//                    HttpRequestUtil.doGet(url1);
//                }
//            }
//        }
//
//    }

    private String getfournorepeatedRandomNum() {
        int[] arr = new int[4];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 10);
            for (int j = 0; j < i; j++) {
                if (arr[i] == arr[j]) {
                    i--;
                    break;
                }
            }
        }
        String num = "";
        for (int i = 0; i < arr.length; i++) {
            num += arr[i];
        }
        return num;
    }



    public String formatName (String name){
        if(name.matches("[\u4E00-\u9FA5]+")){
            List<String> namelist = new ArrayList<>();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                int b = c-0;
                namelist.add(String.valueOf(b));
            }
            String join = String.join("--",namelist);
            return "-"+join+"-";
        }else {
            return name;
        }

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

}
