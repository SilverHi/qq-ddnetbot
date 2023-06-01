package com.silver.ddrtools.bot;


import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.bot.service.BotService;
import com.silver.ddrtools.common.manager.CmdListManager;
import com.silver.ddrtools.common.manager.ThreadPoolManager;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.ddr.task.BeasTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;


/**
 * (tb_book)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/")
@Slf4j
public class BotController {

    @Autowired
    private BotService botService;


    /**
     * 上传图书
     *
     * @return 单条数据
     */
    @PostMapping("/")
    public void getMessage(HttpServletRequest request) {



        //JSONObject
        JSONObject jsonParam = BotUtil.getJSONParam(request);
        log.info("接收参数为:{}",jsonParam.toString() !=null ? jsonParam.toString() : "FALSE");
        //把jsonParam转换成BotMessage对象
        BotMessage botMessage = JSONObject.parseObject(jsonParam.toString(), BotMessage.class);

        String userInput = botMessage.getRaw_message().trim();
        log.info("用户输入的内容为:{}",userInput);
        //处理用户输入
        CmdListManager cmdlist = CmdListManager.getInstance();
        cmdlist.getKeys().forEach(key -> {
            if (userInput.startsWith(key)) {
                    BeasTask task = new BeasTask(botMessage, cmdlist.get(key),key);
                    ThreadPoolManager.getInstance().execute(task);
            }
        });




    }


}
