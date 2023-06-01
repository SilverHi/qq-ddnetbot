package com.silver.ddrtools.game.service;

import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.ddr.service.BeasService;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UmBookinfoService
 * @Description TODO
 * @Author silver
 * @Date 2022/7/17 16:00
 * @Version 1.0
 **/

public interface GuessNumberGameService extends BeasService {
    void guessNumber(BotMessage message);
}
