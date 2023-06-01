package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.common.enums.ResultCode;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.common.util.ResultUtil;
import com.silver.ddrtools.ddr.entity.*;
import com.silver.ddrtools.ddr.service.DDrService;
import com.silver.ddrtools.ddr.service.DdrMapevaluateService;
import com.silver.ddrtools.ddr.service.DdrTilesService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class DDrServiceImpl implements DDrService {


    @Autowired
    SysSettingService sysSettingService;

    @Autowired
    DdrMapevaluateService ddrMapevaluateService;

    @Autowired
    DdrmapinfoService ddrmapinfoService;

    @Autowired
    DdrTilesService ddrTilesService;


//    String imgsavePath = "/botimg/temp.png";



    @Override
    public void updateMapsFromServer(BotMessage message) {

        String DDrmapjsonurl = sysSettingService.getSettingValue("DDrmapjsonurl");
        String messageType = message.getMessage_type();
        String userId = message.getUser_id();
        try {
            String playerjson = HttpRequestUtil.queryDDrGet(DDrmapjsonurl,null,false);
            if (!"error".equals(playerjson)) {
                //解析json
                JSONArray objects = JSONObject.parseArray(playerjson);
                ddrTilesService.deleteAllData();
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
                    List<String> tiles = mapBaseInfo.getTiles();
                    if (!allmaps.stream().anyMatch(map -> map.getMapname().equals(mapBaseInfo.getName()))) {
                        ddrMapsinfo.setGoodnum(0);
                        ddrMapsinfo.setBadnum(0);
                        dDrMapInfos.add(ddrMapsinfo);
                    }
                    if (dDrMapInfos.size() > 800) {
                        ddrmapinfoService.saveBatch(dDrMapInfos);
                        dDrMapInfos.clear();
                    }

                    for (String tile : tiles) {
                        DdrTiles ddrTile = new DdrTiles();
                        ddrTile.setMapName(ddrMapsinfo.getMapname());
                        ddrTile.setTilesName(tile);
                        ddrTiles.add(ddrTile);
                        if (dDrMapInfos.size() > 800) {
                            ddrTilesService.saveBatch(ddrTiles);
                            ddrTiles.clear();
                        }
                    }
                }
                ddrmapinfoService.saveBatch(dDrMapInfos);
                ddrTilesService.saveBatch(ddrTiles);
                //翻译
//                BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "更新地图数据成功");
                return;

            }
        } catch (Exception e) {
            log.error("查询失败", e);
//            BotUtil.autoSwitchSend(messageType, message.getGroup_id(), userId, "更新地图数据失败");
        }


    }








    @Override
    public Map<String, String> getMapsInfoByName(String name) {
        Map<String, String> result = new HashMap<>();
        List<DdrMapinfo> ddrMapinfos = new ArrayList<>();
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mapname", name);
        ddrMapinfos = ddrmapinfoService.list(queryWrapper);
        if (ddrMapinfos.size() == 0) {
            return result;
        }
        DdrMapinfo ddrmapinfo = ddrMapinfos.get(0);
        if (ddrMapinfos != null) {
            result.put("mapname", ddrmapinfo.getMapname());
            result.put("maptype", ddrmapinfo.getMaptype());
            result.put("difficulty", ddrmapinfo.getDifficulty()+"");
            result.put("goodnum", ddrmapinfo.getGoodnum().toString());
            result.put("badnum", ddrmapinfo.getBadnum().toString());
            result.put("img", ddrmapinfo.getThumbnail());
            result.put("previewurl", ddrmapinfo.getWebpreview());
            result.put("mapper", ddrmapinfo.getMapper());
            result.put("points", ddrmapinfo.getPoints()+"");

//            DDrMapRankBean worldmapRank = getMapRank(name, "");
//            if (worldmapRank != null) {
//                result.put("worldfinishers", worldmapRank.getFinishers()+"");
//                result.put("worldmediantime", TimeUtil.secondToTime(worldmapRank.getMedian_time()));
//            }
//
//            DDrMapRankBean chnmapRank = getMapRank(name, "CHN");
//            if (chnmapRank != null) {
//                result.put("chnfinishers", chnmapRank.getFinishers()+"");
//                result.put("chnmediantime", TimeUtil.secondToTime(chnmapRank.getMedian_time()));
//            }

        }

        return result;
    }

    @Override
    public Map<String, String> queryplayer(String playername) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String DDrjsonurl = sysSettingService.getSettingValue("DDrjsonurl");
        try {
            String playerjson = HttpRequestUtil.queryDDrGet(DDrjsonurl,playername,false);
            if (!"error".equals(playerjson)) {
                if ("{}".equals(playerjson)){
                    throw new RuntimeException();
                }
                //解析json
                JSONObject jsonObject = JSONObject.parseObject(playerjson);
                DDrPlayerInfo dDrPlayerInfo = JSON.parseObject(playerjson, DDrPlayerInfo.class);
                DDrQueryUtil.maps2Array(dDrPlayerInfo, jsonObject);

                int points = dDrPlayerInfo.getPoints().getPoints();
                result.put("points", points+"");
            }
        } catch (Exception e) {
            log.error("查询玩家失败", e);
            return result;
        }

        return result;
    }

    @Override
    public ResultBody evaluateMapFromWeb(EvaluateRequest evaluate) {

        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BINARY mapname", evaluate.getMapname());
        DdrMapinfo ddrmapinfo = null;
        List<DdrMapinfo> list = ddrmapinfoService.list(queryWrapper);
        if (list.isEmpty()){
            return ResultUtil.fail(ResultCode.NOT_THIS_MAP);
        }else {
            ddrmapinfo = list.get(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DdrMapevaluate ddrMapevaluate = new DdrMapevaluate();
        ddrMapevaluate.setMapName(evaluate.getMapname());
        ddrMapevaluate.setStar(Integer.valueOf(evaluate.getStarpoint()));
        ddrMapevaluate.setRecommendPoints(evaluate.getRecommendpoints());
        ddrMapevaluate.setContent(evaluate.getTextarea());
        ddrMapevaluate.setTime(sdf.format(new Date()));
        ddrMapevaluate.setUserName(evaluate.getPlayername());
        ddrMapevaluate.setUserPoints(evaluate.getPlayerpoints());
        ddrMapevaluate.setZan(0);
        if (Integer.valueOf(evaluate.getStarpoint())>=4){
            ddrmapinfo.setGoodnum(ddrmapinfo.getGoodnum()+1);
        }else if (Integer.valueOf(evaluate.getStarpoint())<=2){
            ddrmapinfo.setBadnum(ddrmapinfo.getBadnum()+1);
        }
        ddrmapinfoService.saveOrUpdate(ddrmapinfo);
        ddrMapevaluateService.insert(ddrMapevaluate);
        return ResultUtil.success("评价成功");
    }







}
