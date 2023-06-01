package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.CustomWKHtmlToPdfUtil;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DDrPlayerInfo;
import com.silver.ddrtools.ddr.entity.MapInfoBean;
import com.silver.ddrtools.ddr.entity.MapsBean;
import com.silver.ddrtools.ddr.entity.TypesBean;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.service.DDrBeasService;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName QueryFinishedInfoServiceImpl
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 19:35
 * @Version 1.0
 **/
@Slf4j
@Service
public class YearReportServiceImpl extends DDrBeasService {
    @Autowired
    SysSettingService sysSettingService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) {
        String userInputWithoutCmd = cmdParseBean.getUserInputWithoutCmd().trim();
        if (!StringUtil.isBlank(userInputWithoutCmd)) {
            cmdParseBean.setPlayerName(userInputWithoutCmd.trim());
        }
    }
    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws UnexpectedRuntimeException, NetWorkErrorException, InterruptCmdNoReplyException, IOException, InterruptedException {
        String DDrurl = sysSettingService.getSettingValue("DDrurl");
        if (DDrurl == null || DDrurl.equals("")) {
            reply(cmdParseBean, "DDrurl未配置");
            throw new UnexpectedRuntimeException("DDrurl未配置");
        }
        String imgsavePath = sysSettingService.getSettingValue("imgSavePath_" + SystemRunTimeParam.systemName);
        if (imgsavePath == null || imgsavePath.equals("")) {
            reply(cmdParseBean, "imgSavePath未配置");
            throw new UnexpectedRuntimeException("imgSavePath未配置");
        }
        String userName = cmdParseBean.getPlayerName();
        String url = DDrurl + userName + "/";
            String htmlStr = CustomWKHtmlToPdfUtil.getHtmlFromUrl(DDrurl , userName);
            if (!"error".equals(htmlStr)) {
                //是否存在该玩家
                if (!htmlStr.contains("Global Ranks for")) {
                    reply(cmdParseBean, "没有找到【"+cmdParseBean.getPlayerName()+"】该玩家");
                    throw new InterruptCmdNoReplyException("没有找到【"+cmdParseBean.getPlayerName()+"】该玩家");
                }
//                htmlStr = DDrQueryUtil.replaceHtml2LocalHtml(htmlStr);
                if (SystemRunTimeParam.systemName.equals("windows")) {
                    imgsavePath = imgsavePath.substring(1);
                }
                //把html写入文件
                String uuid = UUID.randomUUID().toString();
                String htmlPath = imgsavePath + "/"+uuid+".html";
                CustomWKHtmlToPdfUtil.writeHtml(htmlPath, htmlStr);
                File file = new File(htmlPath);
                Document doc = Jsoup.parse(file, "UTF-8");
                Elements rectElements = doc.select("rect[data-date]");
                HashMap<String, Integer> dayMap = new HashMap<>();
                for (Element rectElement : rectElements) {
                    String dataDate = rectElement.attr("data-date");
                    String dataCount = rectElement.attr("data-count");
                    if (dataDate.startsWith("2022")) {
                        dayMap.put(dataDate, Integer.parseInt(dataCount));
                    }
                }
                //找出活跃时间最长的一天
                int maxDay = 0;
                String maxDayStr = "";
                for (Map.Entry<String, Integer> entry : dayMap.entrySet()) {
                    if (entry.getValue() > maxDay) {
                        maxDay = entry.getValue();
                        maxDayStr = entry.getKey();
                    }
                }
                Integer playCount = 0;
                for (String key : dayMap.keySet()) {
                    if (dayMap.get(key) > 0) {
                        playCount++;
                    }
                }
                //计算活跃月份
                Map<Integer,Integer> activeMonthMap = new HashMap<>();
                for (String key : dayMap.keySet()) {
                    //根据key获取月份
                    String monthStr = key.substring(5,7);
                    if (monthStr.startsWith("0")) {
                        monthStr = monthStr.substring(1);
                    }
                    Integer month = Integer.parseInt(monthStr);
                    if (activeMonthMap.containsKey(month)) {
                        activeMonthMap.put(month, activeMonthMap.get(month) + dayMap.get(key));
                    } else {
                        activeMonthMap.put(month, Integer.valueOf(dayMap.get(key)));
                    }
                }

                List<Integer> activeMonthList = new ArrayList<>();
                for (Integer key : activeMonthMap.keySet()) {
                    if (activeMonthMap.get(key) > 0) {
                        activeMonthList.add(key);
                    }
                }
                StringBuilder activeMonthStr = new StringBuilder();
                if (activeMonthList.size() > 1) {
                    List<String> ranges = getRanges(activeMonthList);
                    activeMonthStr.append("你主要在");
                    for (String range : ranges) {
                        activeMonthStr.append(range).append("月,");
                    }
                    //去掉最后一个逗号
                    activeMonthStr.deleteCharAt(activeMonthStr.length() - 1);
                    activeMonthStr.append("游玩");
                }else {
                    if (activeMonthList.size() >0) {
                        activeMonthStr.append("你主要在"+activeMonthList.get(0)+"月游玩");
                    }
                }
                //计算2022年游戏时长
                Integer playTime = 0;
                for (String key : dayMap.keySet()) {
                    if (key.startsWith("2022")) {
                        playTime += dayMap.get(key);
                    }
                }

                //首次过图
                Elements firstfinishdiv = doc.select("div.block2.ladder:has(h3:contains(First Finish))");
                Element firstfinish = firstfinishdiv.first();
                Elements spanElements = firstfinish.select("span");
                String firstfinishDate = spanElements.first().html();
                Elements aElements = firstfinish.select("a");
                String firstfinishMap = aElements.first().html();
                StringBuilder replyMsg = new StringBuilder();
                replyMsg.append("【"+userName+"】2022年度报告：\n");
                replyMsg.append("你在"+firstfinishDate+"完成了第一张图【"+firstfinishMap+"】，DDnet因为你的加入变得更加繁荣~\n");

                replyMsg.append("2022年你坚持打卡"+playCount+"天，");
                if (playCount == 365){
                    replyMsg.append("2022全勤奖非你莫属，你是DDnet最勤劳的tee！\n");
                }else if (playCount >= 300) {
                    replyMsg.append("你几乎天天在线，过图时大家总能看到你的身影~\n");
                }else  {
                    replyMsg.append(activeMonthStr+"\n");
                }
                replyMsg.append("2022年你在DDnet上累计游玩了"+playTime+"小时，");
                if (playTime >= 1000) {
                    replyMsg.append("努力的tee运气不会太差！\n");
                }else if (playTime < 50) {
                    replyMsg.append("这一年你游玩的时间并不是很长，大家都期望在过图的时候看见你~\n");
                }else {
                    replyMsg.append("这一年你DDnet陪你度过了很多快乐的时光，快说声谢谢DDnet[doge]\n");
                }

                //获取队友
                Elements divElements = doc.select("div.block6.ladder:has(h3:contains(Favorite Partners))");
                Element divElement = divElements.first();
                if (divElement !=null){
                    Elements frinds = divElement.select("a");
                    if (frinds.size() > 1) {
                        replyMsg.append("2022年你最喜欢和"+frinds.first().html()+"一起过图，ta一定是你过图路上最好的搭档！\n");
                        //去掉最后一个逗号
                        replyMsg.deleteCharAt(replyMsg.length() - 1);
                        replyMsg.append("\n");
                    }else{
                        replyMsg.append("2022年的你形影单只，总是和分身过图的你会不会也想找个朋友一起过图呢？\n");
                    }
                }else {
                    replyMsg.append("2022年的你形影单只，总是和分身过图的你会不会也想找个朋友一起过图呢？\n");
                }

                //活跃时间最长一天
                if (maxDay>0){
                    replyMsg.append(maxDayStr+"这天一定很特别，你一共玩了"+maxDay+"个小时，是不是遇到了特别的人或者好玩的图呢？\n");
                }

                String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");

                String urlforPlayr = null;

                String playerjson = HttpRequestUtil.queryDDrGet(DDrjsonurl,cmdParseBean.getPlayerName(),false);
                if (!"error".equals(playerjson)) {
                    if ("{}".equals(playerjson)){
                        reply(cmdParseBean, "未找到【"+cmdParseBean.getPlayerName()+"】玩家");
                        return;
                    }
                    //解析json
                    JSONObject jsonObject = JSONObject.parseObject(playerjson);
                    DDrPlayerInfo dDrPlayerInfo = JSON.parseObject(playerjson, DDrPlayerInfo.class);
                    DDrQueryUtil.maps2Array(dDrPlayerInfo, jsonObject);
                    TypesBean types = dDrPlayerInfo.getTypes();
                    List<MapInfoBean> maplist = types.getMaplist();


                    int maxPlayCount = 0;
                    String maxPlayMap = "";

                    int maxRankCount = 1000000;
                    String maxRankMap = "";
                    int maxRankPoints = 0;

                    String mapName = "";
                    Date firstFinishTime= null;
                    Date tempTime = null;
                    String maxPointsMapName = "";
                    int maxPoints = 0;

                    int pointscount = 0;
                    StringBuilder finishMapTypeReply = new StringBuilder();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (MapInfoBean mapInfoBean : maplist) {
                        List<MapsBean> maps = mapInfoBean.getMaps();
                        String maptype = mapInfoBean.getMap_type();
                        boolean allfinish = true;
                        Date lastFinishTime = null;
                        String lastFinishMapName = "";
                        for (MapsBean mapsBean : maps) {
                            if (mapsBean.getFinishes() > 0) {
                                if(mapsBean.getFinishes()> maxPlayCount){
                                    maxPlayCount = mapsBean.getFinishes();
                                    maxPlayMap = mapsBean.getMap_name();
                                }
                                if (!"DontMove".equals(mapsBean.getMap_name()) && !"DVD screensaver".equals(mapsBean.getMap_name()) && !"Time Calculator".equals(mapsBean.getMap_name())) {
                                    if (mapsBean.getRank() < maxRankCount) {
                                        maxRankCount = mapsBean.getRank();
                                        maxRankMap = mapsBean.getMap_name();
                                        maxRankPoints = mapsBean.getPoints();
                                    }
                                    if (mapsBean.getRank() == maxRankCount) {
                                        if (mapsBean.getPoints() > maxRankPoints) {
                                            maxRankCount = mapsBean.getRank();
                                            maxRankPoints = mapsBean.getPoints();
                                            maxRankMap = mapsBean.getMap_name();
                                        }
                                    }
                                }


                                Integer firstFinish = mapsBean.getFirst_finish();
                                long millis = Long.valueOf(firstFinish) * 1000;
                                Date date = new Date(millis);
                                if (lastFinishTime == null) {
                                    lastFinishTime = date;
                                    lastFinishMapName = mapsBean.getMap_name();
                                } else {
                                    if (lastFinishTime.before(date)) {
                                        lastFinishTime = date;
                                        lastFinishMapName = mapsBean.getMap_name();
                                    }
                                }
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                if (calendar.get(Calendar.YEAR) == 2022) {
                                    pointscount += mapsBean.getPoints();


                                    calendar.set(Calendar.YEAR, 2022);
                                    calendar.set(Calendar.MONTH, 0);
                                    calendar.set(Calendar.DATE, 1);

                                    Calendar start = Calendar.getInstance();
                                    start.set(Calendar.YEAR, 2022);
                                    start.set(Calendar.MONTH, 0);
                                    start.set(Calendar.DATE, 1);
                                    start.set(Calendar.HOUR_OF_DAY, 0);
                                    start.set(Calendar.MINUTE, 0);
                                    start.set(Calendar.SECOND, 0);

                                    Calendar end = Calendar.getInstance();
                                    end.set(Calendar.YEAR, 2022);
                                    end.set(Calendar.MONTH, 0);
                                    end.set(Calendar.DATE, 1);
                                    end.set(Calendar.HOUR_OF_DAY, 6);
                                    end.set(Calendar.MINUTE, 0);
                                    end.set(Calendar.SECOND, 0);
                                    Date startTime = start.getTime();
                                    Date endTime = end.getTime();
                                    if (mapsBean.getPoints() > maxPoints) {
                                        maxPoints = mapsBean.getPoints();
                                        maxPointsMapName = mapsBean.getMap_name();
                                    }
                                    if (calendar.getTime().after(startTime) && calendar.getTime().before(endTime)) {
                                        if (firstFinishTime == null) {
                                            firstFinishTime = date;
                                            mapName = mapsBean.getMap_name();
                                        } else {
                                            Calendar curtTime = Calendar.getInstance();
                                            curtTime.setTime(firstFinishTime);
                                            curtTime.set(Calendar.YEAR, 2022);
                                            curtTime.set(Calendar.MONTH, 0);
                                            curtTime.set(Calendar.DATE, 1);

                                            if (calendar.getTime().after(curtTime.getTime())) {
                                                firstFinishTime = date;
                                                mapName = mapsBean.getMap_name();
                                            }
                                        }
                                    }
                                }



                            }else {
                                allfinish = false;
                            }
                        }
                        if (allfinish){
                            if (lastFinishTime!=null){
                                Calendar lastFinishTimeCalendar = Calendar.getInstance();
                                lastFinishTimeCalendar.setTime(lastFinishTime);
                                if (lastFinishTimeCalendar.get(Calendar.YEAR) == 2022) {
                                    finishMapTypeReply.append("这一年你在"+dateFormat.format(lastFinishTime)+"完成了"+lastFinishMapName+"，从此你跑完了全部的"+ DDrMapType.getZhNameByEnName(maptype) +"图！\n");
                                }
                            }
                        }
                    }

                    if (maxPoints > 0) {
                        replyMsg.append("2022年你过的最难的一张图是"+maxPointsMapName);
                        if (maxPoints > 10) {
                            replyMsg.append("，这张图有" + maxPoints + "分，过了这张图你一定更强了！\n");
                        }else {
                            replyMsg.append("，加油期望你成为最强的那个tee！\n");
                        }
                    }
                    if (firstFinishTime != null) {
                        replyMsg.append(dateFormat.format(firstFinishTime)+"你完成了"+mapName+"，深夜还在过图是不是遇到了什么事呢？\n");
                    }
                    if (maxPlayCount > 0) {
                        replyMsg.append(maxPlayMap+"这张图你一共过了"+maxPlayCount+"次，你一定很喜欢这张图吧？\n");
                    }
                    if (maxRankCount > 0) {
                        replyMsg.append(maxRankMap+"这张图是你成绩最好的一张图，你在这张图世界排名"+maxRankCount+"名\n");
                    }

                    replyMsg.append(finishMapTypeReply);
                    replyMsg.append("2022年你一共获得了"+pointscount+"分，时光如梭，希望明年的我们也能在DDnet中相遇。\n我们2023年见！\n");

                }else {
                    throw new NetWorkErrorException("访问不到ddnet.org");
                }
                reply(cmdParseBean, replyMsg.toString());
            }else {
                throw new NetWorkErrorException("网络异常");
            }
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {

    }

    public  List<String> getRanges(List<Integer> input) {
        List<String> ranges = new ArrayList<>();

        int start = input.get(0);
        int end = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i) == end + 1) {
                end = input.get(i);
            } else {
                ranges.add(getRangeString(start, end));
                start = input.get(i);
                end = input.get(i);
            }
        }
        ranges.add(getRangeString(start, end));

        return ranges;
    }

    public  String getRangeString(int start, int end) {
        if (start == end) {
            return String.valueOf(start);
        }
        return start + "-" + end;
    }
}
