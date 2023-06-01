package com.silver.ddrtools.ddr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.ddr.entity.DDrMapRankBean;
import com.silver.ddrtools.ddr.entity.DdrTiles;
import com.silver.ddrtools.ddr.entity.EvaluateRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UmBookinfoService
 * @Description TODO
 * @Author silver
 * @Date 2022/7/17 16:00
 * @Version 1.0
 **/

public interface DDrService {


    void updateMapsFromServer(BotMessage message);





    Map<String, String> getMapsInfoByName(String name);

    Map<String, String> queryplayer(String playername) throws UnsupportedEncodingException;

    ResultBody evaluateMapFromWeb(EvaluateRequest evaluate);



}
