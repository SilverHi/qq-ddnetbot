package com.silver.ddrtools.ddr.task;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.util.SpringUtil;
import com.silver.ddrtools.ddr.service.BeasService;
import com.silver.ddrtools.ddr.service.impl.QueryFinishedInfoServiceImpl;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName BeasTask
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 17:31
 * @Version 1.0
 **/
@AllArgsConstructor
@Data
public class BeasTask implements Runnable{
    public BotMessage botMessage;

    public String className;

    public String cmd;



    @Override
    public void run() {

        try {
            Class<BeasService> aClass = (Class<BeasService>) Class.forName(className);
            SpringUtil.getBean(aClass).execute(this.botMessage, this.cmd);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

