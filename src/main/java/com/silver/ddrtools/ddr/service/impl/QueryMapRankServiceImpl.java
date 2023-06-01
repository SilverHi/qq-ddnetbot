package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.common.util.TimeUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DDrMapRankBean;
import com.silver.ddrtools.ddr.entity.DDrPlayerInfo;
import com.silver.ddrtools.ddr.entity.MapInfoBean;
import com.silver.ddrtools.ddr.entity.MapsBean;
import com.silver.ddrtools.ddr.entity.TypesBean;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName EvaluateMapServiceImpl
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/29 20:18
 * @Version 1.0
 **/

@Service
@Slf4j
public class QueryMapRankServiceImpl extends DDrBeasService {

    @Autowired
    SysSettingService sysSettingService;

    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException {
        String userInput = cmdParseBean.getUserInputWithoutCmd().trim();
        //判断userInput是否包含双引号
        String playerNameFromUserInput = formatUserInputContentPlayerName(userInput);
        String userInputMapName = "";
        if (!StringUtils.isEmpty(playerNameFromUserInput)) {
            try {
                cmdParseBean.setIsSpacePlayerName(true);
                //去掉playerNameFromUserInput两边的双引号
                String playerName = playerNameFromUserInput.substring(1, playerNameFromUserInput.length() - 1);
                cmdParseBean.setPlayerName(playerName);
                userInputMapName = userInput.replace(playerNameFromUserInput, "").trim();
            }catch (Exception e){
                reply(cmdParseBean, "参数错误，正确格式为：/rank {地图名} [玩家id]  不填id默认查询群名片");
                throw new UserInputCannotFormatException("输入格式错误");
            }
        } else {
            //判断用户知否缺省playername
            cmdParseBean.setIsSpacePlayerName(false);
            userInputMapName = cmdParseBean.getUserInputWithoutCmd().trim();
            if (StringUtil.isBlank(userInputMapName)){
                reply(cmdParseBean, "参数错误，正确格式为：/rank {地图名} [玩家id]  不填id默认查询群名片");
                throw new UserInputCannotFormatException("参数错误");
            }
        }
        if (cmdParseBean.getIsSpacePlayerName()){
            checkMapNameAndReply(userInputMapName,cmdParseBean ,"");
        }else {
            boolean isRightMapName = checkMapName(userInputMapName, cmdParseBean);
            if (!isRightMapName){
                List<String> userinputList = Arrays.asList(cmdParseBean.getUserInputWithoutCmd().trim().split(" "));
                if (userinputList.size() == 1){
                    checkMapNameAndReply(userInputMapName, cmdParseBean,"");
                }else {
                    String playerName = userinputList.get(userinputList.size() - 1);
                    userInputMapName = cmdParseBean.getUserInputWithoutCmd().trim().replace(playerName, "").trim();
                    cmdParseBean.setPlayerName(playerName);
                    checkMapNameAndReply(userInputMapName, cmdParseBean,"如果你的id中包含空格，请用双引号包裹");
                }
            }
        }



    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");
        String playerjson = HttpRequestUtil.queryDDrGet(DDrjsonurl,cmdParseBean.getPlayerName(),false);
        String playerName = cmdParseBean.getPlayerName();
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


            String mapname = cmdParseBean.getMapName();
            StringBuilder repMessages = new StringBuilder("【" + mapname + "】");
            AtomicReference<MapsBean> maps = new AtomicReference<>();
            MapsBean mapsBean = null;
            //获取地图排名
            maplist.forEach(mapInfoBean -> {
                mapInfoBean.getMaps().forEach(mapsBean1 -> {
                    if (mapsBean1.getMap_name().equals(mapname)) {
                        maps.set(mapsBean1);
                    }
                });
            });
            mapsBean = maps.get();

            String map_name = mapsBean.getMap_name();
            //获取排名
            Integer rank = mapsBean.getRank();
            if (rank == null) {
                reply(cmdParseBean, "【" + playerName + "】还未完成【" + map_name + "】");
                return;
            }
            Integer team_rank = mapsBean.getTeam_rank();
            //获取总人数
            Integer total_finishes = mapsBean.getTotal_finishes();
            //计算出百分比
            double v = (double) (total_finishes - rank) / total_finishes;
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(v * 100);
            Double time = mapsBean.getTime();
            //把秒转换成时分秒
            String timeStr = TimeUtil.secondToTime(time);

            //获取地图排名
            DDrMapRankBean chn = getMapRank(map_name, "CHN");
            List<DDrMapRankBean.RanksBean> ranks = chn.getRanks();
            DDrMapRankBean.RanksBean ranksBean = ranks.stream().filter(ranksBean1 -> ranksBean1.getPlayer().equals(playerName)).findFirst().orElse(null);

            List<DDrMapRankBean.TeamRanksBean> team_ranks = chn.getTeam_ranks();
            DDrMapRankBean.TeamRanksBean teamRanksBean = team_ranks.stream().filter(teamRanksBean1 -> teamRanksBean1.getPlayers().contains(playerName)).findFirst().orElse(null);

            StringBuilder rankStr = new StringBuilder("世界排名：" + rank);
            if (team_rank != null) {
                rankStr.append(" - 世界队伍排名：" + team_rank);
            }

            if (chn != null) {
                if (ranksBean != null) {
                    rankStr.append(" - 国服排名：" + ranksBean.getRank());
                }
                if (teamRanksBean != null) {
                    rankStr.append(" - 国服队伍排名：" + teamRanksBean.getRank());
                }
            }

            repMessages.append("总完成人数：" + total_finishes + " \n");


            repMessages.append("【" + cmdParseBean.getPlayerName() + "】- 用时" + timeStr + " - 超过了" + format + "%的玩家\n");
            repMessages.append(rankStr + "\n");
            reply(cmdParseBean, repMessages.toString());
        } else {
            throw new NetWorkErrorException("访问ddnet.org失败");
        }

    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
    }

    public DDrMapRankBean getMapRank(String mapName, String country) {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrRankurl");
        mapName = mapName.replaceAll(" ", "+");
        if (!StringUtils.isEmpty(country)) {
            mapName = mapName + "&country=" + country;
        }
        try {
            String rankJson = HttpRequestUtil.queryDDrGet(DDrjsonurl,mapName,true);
            JSONObject jsonObject = JSON.parseObject(rankJson);
            DDrMapRankBean mapInfoBeans = jsonObject.toJavaObject(DDrMapRankBean.class);
            return mapInfoBeans;
        } catch (Exception e) {
            log.error("获取地图排名失败", e);
        }
        return null;
    }
}
