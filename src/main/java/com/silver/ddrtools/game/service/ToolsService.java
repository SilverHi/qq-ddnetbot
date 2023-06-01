package com.silver.ddrtools.game.service;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.ddr.service.BeasService;

/**
 * @ClassName ToolsService
 * @Description TODO
 * @Author silver
 * @Date 2022/11/18 12:38
 * @Version 1.0
 **/

public interface ToolsService extends BeasService {

    //tools
    void tools(BotMessage message);

}
