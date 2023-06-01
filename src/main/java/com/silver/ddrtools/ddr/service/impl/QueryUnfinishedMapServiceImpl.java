package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.CustomWKHtmlToPdfUtil;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.*;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName QueryUnfinishedMapServiceImpl
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/29 10:52
 * @Version 1.0
 **/
@Slf4j
@Service
public class QueryUnfinishedMapServiceImpl extends DDrBeasService {
    @Autowired
    SysSettingService sysSettingService;
    @Autowired
    DdrmapinfoService ddrmapinfoService;

    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException {
        String cmd = cmdParseBean.getCmd();
        if (cmd.contains("未")) {
            cmdParseBean.setQueryUnfinishedMap(true);
        } else {
            cmdParseBean.setQueryUnfinishedMap(false);
        }
        String userinput = cmdParseBean.getUserInputWithoutCmd().trim();
        String[] userinputs = userinput.split(" ");
        String maptype = userinputs[0];
        Optional<DDrMapType> dDrMapType = DDrMapType.getEnumByZhName(maptype);
        if (dDrMapType.isPresent()) {
            cmdParseBean.setMapType(dDrMapType.get());
        } else {
            throw new UserInputCannotFormatException("用户输入有误");
        }
        if (userinputs.length > 1) {
            String userInputPlyarName = userinput.replace(maptype, "").trim();
            cmdParseBean.setPlayerName(userInputPlyarName);
        }
    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws UnexpectedRuntimeException {
        String DDrurl = sysSettingService.getSettingValue("DDrjsonurl");
        String imgsavePath = sysSettingService.getSettingValue("imgSavePath_" + SystemRunTimeParam.systemName);
        if (imgsavePath == null || imgsavePath.equals("")) {
            reply(cmdParseBean, "请先设置imgSavePath");
            return;
        }
        String htmlStr = HttpRequestUtil.queryDDrGet(DDrurl,cmdParseBean.getPlayerName(),false);
        if (!"error".equals(htmlStr)) {
            if ("{}".equals(htmlStr)){
                reply(cmdParseBean, "未找到【"+cmdParseBean.getPlayerName()+"】玩家");
                return;
            }
            //解析json
            JSONObject jsonObject = JSONObject.parseObject(htmlStr);
            DDrPlayerInfo dDrPlayerInfo = JSON.parseObject(htmlStr, DDrPlayerInfo.class);
            DDrQueryUtil.maps2Array(dDrPlayerInfo, jsonObject);
            TypesBean types = dDrPlayerInfo.getTypes();
            List<MapInfoBean> maplist = types.getMaplist();
            List<String> unfinishedMapNameList = new ArrayList<>();
            List<MapsBean> finishimaps = new ArrayList<>();
            List<MapsBean> allTypemaps = new ArrayList<>();
            for (MapInfoBean mapInfoBean : maplist) {
                if (mapInfoBean.getMap_type().equals(cmdParseBean.getMapType().getEnName())) {
                    List<MapsBean> maps = mapInfoBean.getMaps();
                    allTypemaps = maps;
                    for (MapsBean mapsBean : maps) {
                        if (cmdParseBean.getQueryUnfinishedMap()) {
                            if (mapsBean.getFinishes() == 0) {
                                unfinishedMapNameList.add(mapsBean.getMap_name());
                            }
                        } else {
                            if (mapsBean.getFinishes() > 0) {
                                unfinishedMapNameList.add(mapsBean.getMap_name());
                                finishimaps.add(mapsBean);
                            }
                        }
                    }

                }
            }
            finishimaps.sort(Comparator.comparing(MapsBean::getPoints));

            //从数据库中批量查询地图信息
            LambdaQueryWrapper<DdrMapinfo> aa = new QueryWrapper<DdrMapinfo>().lambda().eq(DdrMapinfo::getMaptype, cmdParseBean.getMapType().getEnName());
            List<DdrMapinfo> ddrMapsinfoAllList = ddrmapinfoService.list(aa);
            List<DdrMapinfo> ddrMapsinfoList = new ArrayList<>();
            for (DdrMapinfo ddrMapsinfo : ddrMapsinfoAllList) {
                if (unfinishedMapNameList.contains(ddrMapsinfo.getMapname())) {
                    ddrMapsinfoList.add(ddrMapsinfo);
                }
            }

            if (ddrMapsinfoList.size() == 0) {
                String repMessage = "";
                if (cmdParseBean.getQueryUnfinishedMap()) {
                    repMessage = "太棒了！【" + cmdParseBean.getPlayerName() + "】已经完成所有的" + cmdParseBean.getMapType().getZhName() + "图啦";
                } else {
                    repMessage = "【" + cmdParseBean.getPlayerName() + "】还没有完成任何" + cmdParseBean.getMapType().getZhName() + "图";
                }
                reply(cmdParseBean, repMessage);
                return;
            }

            ddrMapsinfoList.sort(Comparator.comparing(DdrMapinfo::getDifficulty));

            StringBuilder htmlbuilder = new StringBuilder("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Document</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"./res/maplist.css\">\n" +
                    "</head>");

            htmlbuilder.append("<body>");
            htmlbuilder.append("<h3>" + cmdParseBean.getPlayerName() + " " + cmdParseBean.getMapType().getZhName() + "图" + (cmdParseBean.getQueryUnfinishedMap() ? "未完成" : "已完成") + "列表("+ddrMapsinfoList.size()+"/"+allTypemaps.size()+")：</h3>");


            htmlbuilder.append("<table>");
            htmlbuilder.append("<tr class = \"maintr\">");
            String param = "  ";
            if (cmdParseBean.getQueryUnfinishedMap()){
                if (ddrMapsinfoList.size() <= 50) {
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList));
                    param = " --width 350 ";
                } else if (ddrMapsinfoList.size() > 50 && ddrMapsinfoList.size() <= 100) {
                    int splitnum = ((int) ddrMapsinfoList.size() / 2) + 1;
                    List<DdrMapinfo> ddrMapsinfoList1 = ddrMapsinfoList.subList(0, splitnum);
                    List<DdrMapinfo> ddrMapsinfoList2 = ddrMapsinfoList.subList(splitnum, ddrMapsinfoList.size());
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList1));
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList2));
                    param = " --width 680 ";
                } else {
                    int splitnum = ((int) ddrMapsinfoList.size() / 3) + 1;
                    List<DdrMapinfo> ddrMapsinfoList1 = ddrMapsinfoList.subList(0, splitnum);
                    List<DdrMapinfo> ddrMapsinfoList2 = ddrMapsinfoList.subList(splitnum, splitnum * 2);
                    List<DdrMapinfo> ddrMapsinfoList3 = ddrMapsinfoList.subList(splitnum * 2, ddrMapsinfoList.size());
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList1));
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList2));
                    htmlbuilder.append(DDrQueryUtil.createMapTableHtml(ddrMapsinfoList3));
                }
                htmlbuilder.append("</tr>");
                htmlbuilder.append("</table>");
                htmlbuilder.append("</body>");
                htmlbuilder.append("</html>");
            }else {
                htmlbuilder.append(DDrQueryUtil.createFinishMapTableHtml(finishimaps));
            }



            //把html写入文件
            String filename = UUID.randomUUID().toString();
            String htmlPath = imgsavePath + "/" + filename + ".html";
            //如果是windows去掉htmlPath的第一个/
            if (SystemRunTimeParam.systemName.equals("windows")) {
                htmlPath = htmlPath.substring(1);
            }
            try {
                CustomWKHtmlToPdfUtil.writeHtml(htmlPath, htmlbuilder.toString());
            } catch (IOException e) {
                throw new UnexpectedRuntimeException("写入html文件失败");
            }
            //把html转成图片
            String imgPath = imgsavePath + "/" + filename + ".jpg";
            if (SystemRunTimeParam.systemName.equals("windows")) {
                imgPath = imgPath.substring(1);
            }
            try {
                CustomWKHtmlToPdfUtil.html2imgWithParam(param, htmlPath, imgPath);
            } catch (InterruptedException e) {
                throw new UnexpectedRuntimeException("html转成图片失败");
            } catch (IOException e) {
                throw new UnexpectedRuntimeException("html转成图片失败");
            }
            String rep_message = "[CQ:image,file=file://" + imgsavePath + "/" + filename + ".jpg,id=40000]";
            reply(cmdParseBean, rep_message);
            log.info("未完成图查询成功");
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
