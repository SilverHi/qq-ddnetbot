package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.*;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
public class MapWarringServiceImpl extends DDrBeasService {
    @Autowired
    DdrmapinfoService ddrmapinfoService;
    @Autowired
    SysSettingService sysSettingService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException {
        String userInputWithoutCmd = cmdParseBean.getUserInputWithoutCmd().trim();
        if(userInputWithoutCmd.contains("-list")){
            cmdParseBean.setQueryOwner(false);
            userInputWithoutCmd = userInputWithoutCmd.replace("-list", "").trim();
        }
        if(userInputWithoutCmd.contains("-all")){
            cmdParseBean.setShowAllFlag(true);
            userInputWithoutCmd = userInputWithoutCmd.replace("-all", "").trim();
            if (!StringUtil.isBlank(userInputWithoutCmd)){
                cmdParseBean.setPlayerName(userInputWithoutCmd);
            }
        }


    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");
        StringBuilder repMessages = new StringBuilder();
        //获取所有地图
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().gt(DdrMapinfo::getBadnum, 0).orderByDesc(DdrMapinfo::getBadnum);
        List<DdrMapinfo> badlist = ddrmapinfoService.list(queryWrapper);


        if (cmdParseBean.getQueryOwner()) {
            String userName = cmdParseBean.getPlayerName();
                String playerjson = HttpRequestUtil.queryDDrGet(DDrjsonurl,userName,false);
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
                    List<MapsBean> finishMaps = maplist.stream().map(MapInfoBean::getMaps).flatMap(List::stream).filter(map-> map.getFinishes()!=null && map.getFinishes()>0).collect(Collectors.toList());
                    List<DdrMapinfo> finishbadMaps = badlist.stream().filter(map -> finishMaps.stream().anyMatch(finishMap -> finishMap.getMap_name().equals(map.getMapname()))).collect(Collectors.toList());

                    if (finishbadMaps.size() == 0) {
                        reply(cmdParseBean, "【" + userName + "】 还没有过任何一张粪图");
                        return;
                    }
                    if (!cmdParseBean.getShowAllFlag()) {
                        finishbadMaps = finishbadMaps.stream().limit(10).collect(Collectors.toList());
                    }
                    repMessages.append("【" + userName + "】 过的粪图有：\n");
                    for (DdrMapinfo map : finishbadMaps) {
                        repMessages.append(finishbadMaps.indexOf(map)+1).append(".").append(DDrMapType.getZhNameByEnName(map.getMaptype())+"图 ").append("【").append(map.getMapname()).append("】 ").append(map.getBadnum()).append("人标记为粪图").append("\n");
                    }
                    reply(cmdParseBean, repMessages.toString());

                }else {
                    throw new NetWorkErrorException("网络异常");
                }
        }else {
            String msgHead= "粪图列表:\n";
            if (!cmdParseBean.getShowAllFlag()) {
                msgHead = "粪图列表top10：\n";
                badlist = badlist.stream().limit(10).collect(Collectors.toList());
            }
            repMessages.append(msgHead);
            for (DdrMapinfo map : badlist) {
                repMessages.append(badlist.indexOf(map)+1).append(".").append(DDrMapType.getZhNameByEnName(map.getMaptype())+"图 ").append("【").append(map.getMapname()).append("】 ").append(map.getBadnum()).append("人标记为粪图").append("\n");
            }
            reply(cmdParseBean, repMessages.toString());
        }
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        reply(cmdParseBean, "参数错误，正确格式为：[/]粪图预警 [-all] [-list] [玩家id]");
    }
}
