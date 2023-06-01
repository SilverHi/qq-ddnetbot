package com.silver.ddrtools.ddr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.common.util.CustomWKHtmlToPdfUtil;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DDrPlayerInfo;
import com.silver.ddrtools.ddr.entity.MapInfoBean;
import com.silver.ddrtools.ddr.entity.MapsBean;
import com.silver.ddrtools.ddr.entity.TypesBean;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.service.DDrBeasService;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
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
public class QueryPointsServiceImpl extends DDrBeasService {
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
            String htmlStr = CustomWKHtmlToPdfUtil.getHtmlFromUrl(DDrurl,userName);
            if (!"error".equals(htmlStr)) {
                //是否存在该玩家
                if (!htmlStr.contains("Global Ranks for")) {
                    reply(cmdParseBean, "没有找到【"+cmdParseBean.getPlayerName()+"】该玩家");
                    throw new InterruptCmdNoReplyException("没有找到【"+cmdParseBean.getPlayerName()+"】该玩家");
                }
                htmlStr = DDrQueryUtil.replaceHtml2LocalHtml(htmlStr);
                if (SystemRunTimeParam.systemName.equals("windows")) {
                    imgsavePath = imgsavePath.substring(1);
                }
                //把html写入文件
                String uuid = UUID.randomUUID().toString();
                String htmlPath = imgsavePath + "/"+uuid+".html";
                //如果是windows去掉htmlPath的第一个/
                CustomWKHtmlToPdfUtil.writeHtml(htmlPath, htmlStr);
                //把html转成图片
                String imgPath = imgsavePath + "/"+uuid+".jpg";
                String imgsplitsize = sysSettingService.getSettingValue("imgsplitsize");
                String[] split = imgsplitsize.trim().split(",");

                CustomWKHtmlToPdfUtil.html2imgWithParam(" --crop-h " + split[0] + " --crop-w " + split[1] + " --crop-x " + split[2] + " --crop-y " + split[3] + " ", htmlPath, imgPath);

                if (SystemRunTimeParam.systemName.equals("windows")) {
                    imgsavePath = "/" + imgsavePath;
                }
                System.out.println("succes");
                String rep_message = "[CQ:image,file=file://" + imgsavePath + "/"+uuid+".jpg,id=40000]";
                reply(cmdParseBean, rep_message);
            }else {
                throw new NetWorkErrorException("网络异常");
            }
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {

    }

}
