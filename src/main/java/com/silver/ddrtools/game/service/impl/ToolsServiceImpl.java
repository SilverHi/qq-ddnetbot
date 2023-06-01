package com.silver.ddrtools.game.service.impl;

import com.google.errorprone.annotations.Var;
import com.silver.ddrtools.bot.entity.BotMessage;
import com.silver.ddrtools.common.entity.ChatBotBean;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.BotUtil;
import com.silver.ddrtools.common.util.ChatGptUtil;
import com.silver.ddrtools.common.util.HttpRequestUtil;
import com.silver.ddrtools.ddr.enums.VoiceCode;
import com.silver.ddrtools.game.dao.ToolsWhateatingMapper;
import com.silver.ddrtools.game.entity.ToolsWhateating;
import com.silver.ddrtools.game.service.ToolsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName ToolsServiceImpl
 * @Description TODO
 * @Author silver
 * @Date 2022/11/18 12:38
 * @Version 1.0
 **/
@Service
public class ToolsServiceImpl implements ToolsService {


    @Autowired
    private ToolsWhateatingMapper whateatingMapper;
    @Autowired
    private SysSettingService sysSettingService;

    boolean unfinish = true;

    String messageType;
    String userId;
    String groupId;
    String userInput;

    @Override
    public void tools(BotMessage message) {
        getInputBaseInfo(message);
        if (unfinish) {
            dice(message);
        }
        if (unfinish) {
            changeGroupTitle(message);
        }
        if (unfinish) {
            changeGroupCard(message);
        }
        if (unfinish) {
            whatToEat(message);
        }
        if (unfinish) {
            cmdList(message);
        }
        if (unfinish) {
            voiceList(message);
        }
        if (unfinish) {
            duoshe(message);
        }
        if (unfinish) {
            tts(message);
        }
        if (unfinish) {
            chattoGPT(message);
        }
        if (unfinish) {
            getImage(message);
        }
        if (unfinish) {
            reset(message);
        }

    }

    private void voiceList(BotMessage message) {
        if (userInput.startsWith("/模型列表")) {
            String cmdList = sysSettingService.getSettingValue("voiceList");
            BotUtil.autoSwitchSend(messageType, groupId, userId, cmdList);
            unfinish = false;
        }
    }

    private void tts(BotMessage message) {
        if (userInput.startsWith("/tts")) {
            String param = userInput.replace("/tts", "").trim();
            if (param.startsWith("-")){
                param = param.replaceFirst("-","");
                String[] s = param.split(" ", 2);
                Optional<VoiceCode> voiceByName = VoiceCode.getVoiceByName(s[0]);
                if (voiceByName.isPresent()){
                    dotts(s[1],voiceByName.get().getCode());
                }else {
                    BotUtil.autoSwitchSend(messageType, groupId, userId, "指定的语音模型【"+s[0]+"】不存在");
                    unfinish = false;
                }
                dotts(s[1],s[0]);
            }else {
                dotts(param,null);
            }
            unfinish = false;
        }
    }

    private String dotts(String param,String voice) {
        String path = "/home/ddrtools/test.webm";
        if (SystemRunTimeParam.systemName.equals("windows")) {
            path = "D:\\test\\test.webm";
        }
        try {
            HttpRequestUtil.tts(param,path,voice);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (SystemRunTimeParam.systemName.equals("windows")) {
            path = "/"+path;
        }
        try {
            BotUtil.autoSwitchSend(messageType, groupId, userId, "[CQ:record,file=file://"+path+"]");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private void chattoGPT(BotMessage message) {
        if (userInput.trim().startsWith("/chat")) {
            String param = userInput.replace("/chat", "").trim();
            try {
                boolean isText = false;
                String type = null;
                if(param.trim().startsWith("-text")){
                    isText = true;
                    param = param.replace("-text","");
                }
                if(param.trim().startsWith("-ddr")){
                    type = "ddr";
                    param = param.replace("-ddr","");
                }
                if(param.trim().startsWith("-re")){
                    type = "recovery";
                    param = param.replace("-re","");
                }

                String result = ChatGptUtil.getresponse(userId,type,param);

                if (!isText && result.length()<200  ){
                    dotts(result,null);
                }else {
                    BotUtil.autoSwitchSend(messageType, groupId, userId, result);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            unfinish = false;
        }
    }

    private void reset(BotMessage message) {
        if (userInput.startsWith("/reset")) {
            String param = userInput.replace("/reset", "").trim();
            ChatGptUtil.reset(userId);
            BotUtil.autoSwitchSend(messageType, groupId, userId,"对话已重置" );
            unfinish = false;
        }
    }

    private void getImage(BotMessage message) {
        if (userInput.trim().startsWith("/image")) {
            String param = userInput.replace("/image", "").trim();
            try {
                String image = HttpRequestUtil.createImage(param);
                BotUtil.autoSwitchSend(messageType, groupId, userId,"[CQ:image,file=file:///"+image+",type=show,id=40000]" );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            unfinish = false;
        }
    }

    public void cmdList(BotMessage message){

        if (userInput.startsWith("/指令")) {
            String cmdList = sysSettingService.getSettingValue("cmdList");
            BotUtil.autoSwitchSend(messageType, groupId, userId, cmdList);
            unfinish = false;
        }
    }

    public void getInputBaseInfo(BotMessage message) {
        userInput = message.getRaw_message().trim();
        messageType = message.getMessage_type();
        groupId = message.getGroup_id() == null ? "" : message.getGroup_id();
        userId = message.getUser_id() == null ? "" : message.getUser_id();
        unfinish = true;
    }

    //骰子
    public void dice(BotMessage message) {
        if (userInput.startsWith("/骰子")) {
            String param = userInput.replace("/骰子", "").trim();
            //是否有参数
            if (param.length() < 1) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "骰子结果为：" + (int) (Math.random() * 6 + 1));
                unfinish = false;
                return;
            } else {
                try {
                    //判断param是否为数字
                    if (param.matches("[0-9]+")) {
                        int paramInt = Integer.parseInt(param);
                        if (paramInt > 0) {
                            BotUtil.autoSwitchSend(messageType, groupId, userId, "骰子结果为：" + (int) (Math.random() * paramInt + 1));
                            unfinish = false;
                            return;
                        } else {
                            BotUtil.autoSwitchSend(messageType, groupId, userId, "骰子参数必须大于0");
                            unfinish = false;
                            return;
                        }
                    } else {
                        BotUtil.autoSwitchSend(messageType, groupId, userId, "骰子参数必须为数字");
                        unfinish = false;
                        return;
                    }
                } catch (Exception e) {
                    BotUtil.autoSwitchSend(messageType, groupId, userId, "骰子参数错误");
                    unfinish = false;
                    return;
                }
            }


        }

    }

    //今天吃什么
    public void whatToEat(BotMessage message) {

        if (userInput.startsWith("/今天吃什么")) {
            //从数据库中随机获取一条数据
            Integer integer = whateatingMapper.selectCount(null);
            Long l = Long.valueOf((int)(Math.random() * integer + 1)+"");
            ToolsWhateating whateating = whateatingMapper.selectByPrimaryKey(l);
            BotUtil.autoSwitchSend(messageType, groupId, userId, "吃"+whateating.getFoodname());
            BotUtil.autoSwitchSend(messageType, groupId, userId, "[CQ:image,file=file://"+whateating.getImgpath()+",id=40000]");
            unfinish = false;
            return;
        }


    }

    //修改群头衔
    public void changeGroupTitle(BotMessage message) {

        if (userInput.startsWith("/改头衔")) {
            //私聊不支持
            if (messageType.equals("private")) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "私聊不支持修改群头衔");
                unfinish = false;
                return;
            }
            String param = userInput.replace("/改头衔", "").trim();
            //是否有参数
            if (param.length() < 1) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "请输入要修改的群头衔");
                unfinish = false;
                return;
            } else {
                BotUtil.setGroupSpecialCard(groupId, userId, param);
                BotUtil.sendGroup(groupId, "群头衔修改成功");
                unfinish = false;
                return;
            }
        }
    }
    //

    //修改群名片
    public void changeGroupCard(BotMessage message) {
        if (userInput.startsWith("/改备注")) {
            //私聊不支持
            if (messageType.equals("private")) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "私聊不支持修改群名片");
                unfinish = false;
            }
            String param = userInput.replace("/改备注", "").trim();
            //是否有参数
            if (param.length() < 1) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "请输入要修改的群名片");
                unfinish = false;
                return;
            } else {
                BotUtil.setGroupCard(groupId, userId, param);
                BotUtil.sendGroup(groupId, "群名片修改成功");
                unfinish = false;
                return;
            }
        }
    }

    //夺舍说话
    public void duoshe(BotMessage message) {
        if (userInput.startsWith("/夺舍说话")) {
            String param = userInput.replace("/夺舍说话", "").trim();
            String[] split = param.split(" ");
            String togroupid = split[0];
            String text = split[1];

            //是否有参数
            if (split.length < 1) {
                BotUtil.autoSwitchSend(messageType, groupId, userId, "请输入要发送的群号和内容");
                unfinish = false;
                return;
            } else {
                BotUtil.sendGroup(togroupid, text);
                unfinish = false;
                return;
            }
        }
    }


    @Override
    public void execute(BotMessage message, String cmd) {
        tools(message);
    }
}
