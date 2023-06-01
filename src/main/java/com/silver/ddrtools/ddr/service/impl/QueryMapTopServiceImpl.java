package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.common.util.TimeUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DDrMapRankBean;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class QueryMapTopServiceImpl extends DDrBeasService {
    @Autowired
    DdrmapinfoService ddrmapinfoService;

    @Autowired
    SysSettingService sysSettingService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException, UnexpectedRuntimeException {
        String userInputWithoutCmd = cmdParseBean.getUserInputWithoutCmd().trim();

        if(userInputWithoutCmd.contains("-chn")){
            cmdParseBean.setChnFlag(true);
            userInputWithoutCmd = userInputWithoutCmd.replace("-chn", "");
        }
        if(userInputWithoutCmd.contains("-team")){
            cmdParseBean.setTeamFlag(true);
            userInputWithoutCmd = userInputWithoutCmd.replace("-team", "");
        }
        String userInputmapName = userInputWithoutCmd.trim();
        if (StringUtil.isBlank(userInputmapName)) {
            throw new UserInputCannotFormatException("请输入地图名");
        }
        checkMapNameAndReply(userInputmapName, cmdParseBean,"");
    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String mapName = cmdParseBean.getMapName();
        String country = "";
        if(cmdParseBean.getChnFlag()){
            country = "CHN";
        }
        DDrMapRankBean mapRank = getMapRank(mapName, country);
        if (mapRank == null) {
            throw new NetWorkErrorException("网络异常");
        }
        List<DDrMapRankBean.RanksBean> ranks = mapRank.getRanks();
        StringBuilder repMessages = new StringBuilder();
        String teamstr= cmdParseBean.getTeamFlag() ? "队伍" : "个人";
        if(StringUtils.isBlank(country)) {
            repMessages.append("【" + mapName + "】世界"+teamstr+"排名：\n");

        }else {
            repMessages.append("【" + mapName + "】国服"+teamstr+"排名：\n");
        }
        repMessages.append("总完成人数：" + mapRank.getFinishers() + " - 平均用时:"+ TimeUtil.secondToTime(mapRank.getMedian_time())+" \n");
        if (cmdParseBean.getTeamFlag()) {
            //队伍排名
            List<DDrMapRankBean.TeamRanksBean> team_ranks = mapRank.getTeam_ranks();
            for (DDrMapRankBean.TeamRanksBean teamRanksBean : team_ranks) {
                repMessages.append( + teamRanksBean.getRank() + ". " + String.join("  ",teamRanksBean.getPlayers())+ "  Time:" + TimeUtil.secondToTime(teamRanksBean.getTime()) +"\n");
            }
        } else {
            for (DDrMapRankBean.RanksBean ranksBean : ranks) {
                repMessages.append( + ranksBean.getRank() + ". " + ranksBean.getPlayer() + "  Time:" + TimeUtil.secondToTime(ranksBean.getTime()) +"\n");
            }
        }
        reply(cmdParseBean,repMessages.toString());
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        reply(cmdParseBean, "参数错误，正确格式为： [/]top [-chn| -team] {地图名}");
    }

    public DDrMapRankBean getMapRank(String mapName,String country) {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrRankurl");
        mapName = mapName.replaceAll(" ", "+");
        if (!StringUtils.isEmpty(country)) {
            mapName = mapName+"&country="+country;
        }
        try {
            String rankJson = HttpRequestUtil.queryDDrGet(DDrjsonurl,mapName,true);
            JSONObject jsonObject = JSON.parseObject(rankJson);
            DDrMapRankBean mapInfoBeans = jsonObject.toJavaObject(DDrMapRankBean.class);
            return mapInfoBeans;
        }catch (Exception e) {
            log.error("获取地图排名失败", e);
        }
        return null;
    }

}
