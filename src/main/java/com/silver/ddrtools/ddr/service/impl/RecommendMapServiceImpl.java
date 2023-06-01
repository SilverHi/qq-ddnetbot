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
public class RecommendMapServiceImpl extends DDrBeasService {
    @Autowired
    DdrmapinfoService ddrmapinfoService;
    @Autowired
    SysSettingService sysSettingService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException {
        String userInputWithoutCmd = cmdParseBean.getUserInputWithoutCmd().trim();
        if(userInputWithoutCmd.contains("-all")){
            cmdParseBean.setShowAllFlag(true);
        }
        userInputWithoutCmd = userInputWithoutCmd.replace("-all", "").trim();
        if (!StringUtil.isBlank(userInputWithoutCmd)){
            cmdParseBean.setPlayerName(userInputWithoutCmd);
        }
    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");
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

            //获取所有地图
            QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().gt(DdrMapinfo::getGoodnum, 0).orderByAsc(DdrMapinfo::getPoints).orderByDesc(DdrMapinfo::getGoodnum);
            List<DdrMapinfo> list = ddrmapinfoService.list(queryWrapper);
            List<MapsBean> finishMaps = maplist.stream().map(MapInfoBean::getMaps).flatMap(List::stream).filter(map-> map.getFinishes()!=null && map.getFinishes()>0).collect(Collectors.toList());
            StringBuilder repMessages = new StringBuilder("【" + userName + "】  "+dDrPlayerInfo.getPoints().getPoints()+"分\n");
            list.removeIf(map -> finishMaps.stream().anyMatch(finishMap -> finishMap.getMap_name().equals(map.getMapname())));
            //list取前10
            if (!cmdParseBean.getShowAllFlag()) {
                list = list.stream().limit(10).collect(Collectors.toList());
            }
            for (DdrMapinfo map : list) {
                repMessages.append(DDrMapType.getZhNameByEnName(map.getMaptype())+"图 ").append("【").append(map.getMapname()).append("】 ").append(map.getGoodnum()).append("人推荐").append("\n");
            }
            if (list.size() == 0) {
                repMessages.append("所有标记为好图的地图，你都已经过啦~");
            }
            log.info("过图推荐查询成功");
            //翻译
            String repMessage = repMessages.toString();
            String translate = DDrQueryUtil.translate(repMessage);
            reply(cmdParseBean,translate);

        }else{
            throw new NetWorkErrorException("网络错误");
        }
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        reply(cmdParseBean, "参数错误，正确格式为：[/]过图推荐 [-all] [玩家id]");
    }
}
