package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DDrPlayerInfo;
import com.silver.ddrtools.ddr.entity.MapInfoBean;
import com.silver.ddrtools.ddr.entity.MapsBean;
import com.silver.ddrtools.ddr.entity.TypesBean;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.service.CmdBeasService;
import com.silver.ddrtools.ddr.service.DDrBeasService;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
public class QueryFinishedInfoServiceImpl extends DDrBeasService {
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
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws NetWorkErrorException {
        String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");
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


            StringBuilder repMessages = new StringBuilder("【" + cmdParseBean.getPlayerName() + "】  "+dDrPlayerInfo.getPoints().getPoints()+"分\n");

            for (MapInfoBean mapInfoBean : maplist) {
                repMessages.append(mapInfoBean.getMap_type() + "图共：" + mapInfoBean.getPoints().getTotal() + "分,已获得" + (mapInfoBean.getPoints().getPoints() == null ? "0" : mapInfoBean.getPoints().getPoints()) + "分，已完成：");
                List<MapsBean> maps = mapInfoBean.getMaps();
                int finishedCount = 0;
                for (MapsBean mapsBean : maps) {
                    if (mapsBean.getFinishes() > 0) {
                        finishedCount++;
                    }
                }
                repMessages.append(finishedCount + "/" + maps.size() + "\n");
            }
            log.info("过图情况查询成功");
            //翻译
            String repMessage = repMessages.toString();
            String translate = DDrQueryUtil.translate(repMessage);
            reply(cmdParseBean, translate);
        }else {
            throw new NetWorkErrorException("访问不到ddnet.org");
        }
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {

    }

}
