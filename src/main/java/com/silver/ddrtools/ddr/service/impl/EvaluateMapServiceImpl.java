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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class EvaluateMapServiceImpl extends DDrBeasService {
    @Autowired
    DdrmapinfoService ddrmapinfoService;
    @Override
    public void parseCmd(BotMessage message, CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException {
        String userInputWithoutCmd = cmdParseBean.getUserInputWithoutCmd().trim();
        List<String> userInputList = Arrays.asList(userInputWithoutCmd.split(" "));
        //取userInputList最后一个元素是好图还是粪图
        String lastElement = userInputList.get(userInputList.size() - 1).trim();
        if ("好图".equals(lastElement)){
            cmdParseBean.setIsGoodMap(true);
        }else if ("粪图".equals(lastElement)){
            cmdParseBean.setIsGoodMap(false);
        }else {
            throw new UserInputCannotFormatException("输入格式错误，最后一个参数必须是好图或者粪图");
        }
        String userInputmapName = userInputWithoutCmd.replace(lastElement, "").trim();
        checkMapNameAndReply(userInputmapName,cmdParseBean,"");
    }

    @Override
    public void doCmd(BotMessage message, CmdParseBean cmdParseBean) throws Exception {
        String mapName = cmdParseBean.getMapName();
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<DdrMapinfo>().eq("BINARY mapname", mapName);
        DdrMapinfo ddrmapinfo =null;
        List<DdrMapinfo> list = ddrmapinfoService.list(queryWrapper);
        if (list.isEmpty()){
            throw new UnexpectedRuntimeException("数据库中没有这个图");
        }else {
            ddrmapinfo = list.get(0);
        }
        if (cmdParseBean.getIsGoodMap()) {
            ddrmapinfo.setGoodnum((ddrmapinfo.getGoodnum()==null?0:ddrmapinfo.getGoodnum() )+ 1);
        } else{
            ddrmapinfo.setBadnum((ddrmapinfo.getBadnum()==null?0:ddrmapinfo.getBadnum() ) + 1);
        }
        reply(cmdParseBean,"【" + mapName + "】确实是个" + (cmdParseBean.getIsGoodMap()?"好图":"粪图") + "。我已经记下来了。");
        ddrmapinfoService.updateById(ddrmapinfo);
    }

    @Override
    public void errorReply(CmdParseBean cmdParseBean) {
        reply(cmdParseBean, "参数错误，正确格式为：[/]过图评价 {地图名} {好图|粪图}");
    }
}
