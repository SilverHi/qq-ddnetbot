package com.silver.ddrtools.ddr.service;

import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;
import com.silver.ddrtools.ddr.exception.NetWorkErrorException;
import com.silver.ddrtools.ddr.exception.UnexpectedRuntimeException;
import com.silver.ddrtools.ddr.exception.UserInputCannotFormatException;
import com.silver.ddrtools.ddr.task.BeasTask;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CmdBeasService
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 16:10
 * @Version 1.0
 **/
@Slf4j
public abstract class CmdBeasService implements BeasService {
    //解析命令
    public void parseCmdBefor(BotMessage message,CmdParseBean cmdParseBean){
        cmdParseBean.setMessageType(message.getMessage_type());
        cmdParseBean.setGroupId(message.getGroup_id());
        cmdParseBean.setUserId(message.getUser_id());
        String userInput = message.getRaw_message().trim();
        cmdParseBean.setUserInputWithoutCmd(userInput.replace(cmdParseBean.getCmd(), ""));
        //默认查询的玩家名
        if (BotMessage.PRIVATE.equals(message.getMessage_type())) {
            cmdParseBean.setPlayerName(message.getSender().getNickname());
        } else if (BotMessage.GROUP.equals(message.getMessage_type())) {
            String card = message.getSender().getCard();
            if (card != null && !"".equals(card)) {
                cmdParseBean.setPlayerName(card);
            } else {
                cmdParseBean.setPlayerName(message.getSender().getNickname());
            }
        }
    }
    public abstract void parseCmd(BotMessage message,CmdParseBean cmdParseBean) throws UserInputCannotFormatException, InterruptCmdNoReplyException, UnexpectedRuntimeException;

    public abstract void doCmd(BotMessage message,CmdParseBean cmdParseBean) throws Exception;
    public CmdParseBean getParseBean(String cmd){
        CmdParseBean cmdParseBean = new CmdParseBean();
        cmdParseBean.setCmd(cmd);
        return cmdParseBean;
    }

    public void execute(BotMessage message,String cmd) {
        CmdParseBean cmdParseBean = getParseBean(cmd);
        parseCmdBefor(message,cmdParseBean);
        try {
            parseCmd(message, cmdParseBean);
            doCmd(message, cmdParseBean);
        }catch (InterruptCmdNoReplyException e){
            log.debug("中断命令执行");
        }catch (UserInputCannotFormatException e){
            errorReply(cmdParseBean);
        }catch (NetWorkErrorException e){
            reply(cmdParseBean,"哦豁,访问ddnet.org失败,网络抽风了,再试一次");
        }catch (UnexpectedRuntimeException e){
            finalErrorReply(cmdParseBean);
            log.error("bug:",e);
        } catch (Exception e){
            finalErrorReply(cmdParseBean);
            log.error("bug:",e);
        }


    }

    public void reply(CmdParseBean cmdParseBean,String replyMsg){
        BotUtil.autoSwitchSend(cmdParseBean.getMessageType(), cmdParseBean.getGroupId(), cmdParseBean.getUserId(),replyMsg);
    }

    public abstract void errorReply(CmdParseBean cmdParseBean) ;

    public void finalErrorReply(CmdParseBean cmdParseBean){
        if (cmdParseBean.getMessageType().equals(BotMessage.GROUP)){
            reply(cmdParseBean,"出现了意料之外的错误，[CQ:at,qq=403896948] 出来干活了");
        }else if (cmdParseBean.getMessageType().equals(BotMessage.PRIVATE)){
            reply(cmdParseBean,"出现了意料之外的错误");

        }
    }

    //根据userInput判断是否包含中文双引号或者英文双引号，若包含则返回双引号中的内容和对应的双引号，若不包含则返回空字符串

    public String formatUserInputContentPlayerName(String userInput) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        for (char c : userInput.toCharArray()) {
            if (c == '"' || c == '“' || c == '”') {
                if (insideQuotes) {
                    insideQuotes = false;
                }
                else {
                    insideQuotes = true;
                }
                result.append(c);
            }
            else if (insideQuotes) {
                result.append(c);
            }
        }
        if (insideQuotes) {
            return null;
        }
        return result.toString();
    }
}
