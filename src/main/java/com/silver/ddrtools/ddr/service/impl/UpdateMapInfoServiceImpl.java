package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.common.util.CustomWKHtmlToPdfUtil;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.*;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName QueryUnfinishedMapServiceImpl
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/29 10:52
 * @Version 1.0
 **/
@Slf4j
@Service
public class UpdateMapInfoServiceImpl extends DDrBeasService {
    @Autowired
    SysSettingService sysSettingService;
    @Autowired
    DdrmapinfoService ddrmapinfoService;

    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException {

    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws UnexpectedRuntimeException {

        String DDrmapjsonurl = sysSettingService.getSettingValue("DDrmapjsonurl");
        String messageType = message.getMessage_type();
        String userId = message.getUser_id();
        try {
            String mapinfo = HttpRequestUtil.doddrGet(DDrmapjsonurl);
            if (!StringUtil.isBlank(mapinfo) && !"error".equals(mapinfo)) {
                //解析json
                JSONArray objects = JSONObject.parseArray(mapinfo);
                List<DdrMapinfo> dDrMapInfos = new ArrayList<>();
                List<DdrTiles> ddrTiles = new ArrayList<>();
                List<DdrMapinfo> allmaps = ddrmapinfoService.list();
                for (Object object : objects) {
                    MapBaseInfo mapBaseInfo = JSONObject.parseObject(object.toString(), MapBaseInfo.class);
                    DdrMapinfo ddrMapsinfo = new DdrMapinfo();
                    ddrMapsinfo.setMapname(mapBaseInfo.getName());
                    ddrMapsinfo.setMapper(mapBaseInfo.getMapper());
                    ddrMapsinfo.setDifficulty(mapBaseInfo.getDifficulty());
                    ddrMapsinfo.setPoints(mapBaseInfo.getPoints());
                    ddrMapsinfo.setHeight(mapBaseInfo.getHeight());
                    ddrMapsinfo.setWidth(mapBaseInfo.getWidth());
                    ddrMapsinfo.setMaprelease(mapBaseInfo.getRelease());
                    ddrMapsinfo.setThumbnail(mapBaseInfo.getThumbnail());
                    ddrMapsinfo.setMaptype(mapBaseInfo.getType());
                    ddrMapsinfo.setWebpreview(mapBaseInfo.getWeb_preview());
                    ddrMapsinfo.setWebsite(mapBaseInfo.getWebsite());
                    if (!allmaps.stream().anyMatch(map -> map.getMapname().equals(mapBaseInfo.getName()))) {
                        ddrMapsinfo.setGoodnum(0);
                        ddrMapsinfo.setBadnum(0);
                        dDrMapInfos.add(ddrMapsinfo);
                    }
                    if (dDrMapInfos.size() > 800) {
                        ddrmapinfoService.saveBatch(dDrMapInfos);
                        dDrMapInfos.clear();
                    }

                }
                ddrmapinfoService.saveBatch(dDrMapInfos);
                reply(cmdParseBean, "更新地图数据成功");
            }
        } catch (Exception e) {
            log.error("查询失败", e);
            reply(cmdParseBean, "更新地图数据失败");
        }


    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        StringBuilder errorMessage = new StringBuilder("请输入正确的格式：/{未完成图|已完成图} {地图类型} {id}\n");
        errorMessage.append("地图类型可选参数为：\n");
        for (DDrMapType dDrMapType : Arrays.asList(DDrMapType.values())) {
            errorMessage.append(dDrMapType.getZhName() + "\n");
        }
        reply(cmdParseBean, errorMessage.toString());
    }
}
