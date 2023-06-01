package com.silver.ddrtools.ddr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.entity.DdrMapinfo;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.service.DDrBeasService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

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
public class QueryMapEvaluateServiceImpl extends DDrBeasService {
    @Autowired
    DdrmapinfoService ddrmapinfoService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException {
        String userInputmapName = cmdParseBean.getUserInputWithoutCmd().trim();
        if (StringUtil.isBlank(userInputmapName)) {
            throw new UserInputCannotFormatException("请输入地图名");
        }
        checkMapNameAndReply(userInputmapName,cmdParseBean,"");
    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String mapName = cmdParseBean.getMapName();
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<DdrMapinfo>().eq("BINARY mapname", mapName);
        DdrMapinfo ddrmapinfo = null;
        List<DdrMapinfo> list = ddrmapinfoService.list(queryWrapper);
        if (list.isEmpty()){
            throw new UnexpectedRuntimeException("数据库中没有这个图");
        }else {
            ddrmapinfo = list.get(0);
        }
        String goodnum = ddrmapinfo.getGoodnum() == null ? "0" : ddrmapinfo.getGoodnum().toString();
        String badnum = ddrmapinfo.getBadnum() == null ? "0" : ddrmapinfo.getBadnum().toString();

        if (goodnum.equals("0") && badnum.equals("0")) {
            reply(cmdParseBean, "还没有人评价过【" + mapName + "】");
        } else {
            //介绍
            String goodintroduce = "";
            if (goodnum.equals("0")){
                goodintroduce = "还没有人评价过好图";
            }else {
                goodintroduce = "被" + goodnum + "次标记为好图";
            }
            String badintroduce = "";
            if (badnum.equals("0")) {
                badintroduce = "还没有人评价过粪图";
            } else {
                badintroduce = "被" + badnum + "次标记为粪图";
            }
            reply(cmdParseBean, "【" + mapName + "】" + goodintroduce + "，" + badintroduce);
        }

    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        reply(cmdParseBean, "参数错误，正确格式为：[/]查询评价 {地图名} ");
    }
}
