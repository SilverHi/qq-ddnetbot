package com.silver.ddrtools.common.util;


import com.alibaba.fastjson.JSONArray;
import com.github.plexpt.chatgpt.Chatbot;
import com.silver.ddrtools.common.entity.ChatBotBean;

import com.silver.ddrtools.common.entity.Message;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChatGptUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/12/6 15:36
 * @Version 1.0
 **/

@Slf4j
public class ChatGptUtil {

    public static Map<String, ChatBotBean> chatbotMap = new HashMap<>();
    public static String apikey = "sk-GQDG6bJUxaCNVTp3Jt6d1XQmMCwmbHw2n6zpIBOILjov9Yn3";


    public static ChatBotBean getChatBot(String userid,String type) {
        //chatbotMap中找，如果不存在，就创建一个
        if (!chatbotMap.containsKey(userid)) {
            if ("ddr".equals(type)) {
                chatbotMap.put(userid, new ChatBotBean(userid, true, ddrprompt()));
            }else if ("recovery".equals(type)) {
                chatbotMap.put(userid, new ChatBotBean(userid, true, recoveryprompt()));
            }else {
                chatbotMap.put(userid, new ChatBotBean(userid, true, new JSONArray()));
            }
        }
        return chatbotMap.get(userid);
    }

    public static String getresponse(String userid,String type,String context) {
        //取出chatbot
        ChatBotBean chatbot = getChatBot(userid,type);
        Boolean aBoolean;
        if (chatbot.getIsReply()) {
            chatbot.setIsReply(false);
            Message message = new Message("user", context);
            chatbot.getContext().add(message);
            aBoolean = HttpRequestUtil.chattoGPT(chatbot);
        }else {
            return "请求chatgpt中，请等待回复";
        }
        if (!aBoolean) {
            return "chatbot响应失败";
        }
        //垃圾回收需要进行ai监管
        if ("recovery".equals(type)) {
            String rp = chatbot.getContext().getJSONObject(chatbot.getContext().size()-1).getString("content");
            Boolean regulation = HttpRequestUtil.regulation(aiprompt(), rp);
            chatbot.setIsReply(true);
            if (regulation) {
                return rp;
            }else {
                //删除对话
                JSONArray context1 = chatbot.getContext();
                context1.remove(context1.size()-1);
                context1.remove(context1.size()-1);
                return "对不起，我只能回答关于垃圾回收的问题";
            }

        }
        chatbot.setIsReply(true);
        String rp = chatbot.getContext().getJSONObject(chatbot.getContext().size()-1).getString("content");
        System.out.println(rp);
        return rp;
    }
    public static void reset(String userid) {
        chatbotMap.remove(userid);
    }


    public static void setToken(String token) {
        ChatGptUtil.apikey = token;
        chatbotMap.clear();
    }

    public static  JSONArray ddrprompt() {
        JSONArray prompt = new JSONArray();
        Message message = new Message("system","你是一个DDRaceNetWork游戏的客服助手，请你帮助回答关于DDRaceNetWork的问题");
        prompt.add(message);
        return prompt;
    }

    public static  JSONArray recoveryprompt() {
        JSONArray prompt = new JSONArray();
        Message message = new Message("system","你是一个帮助用户分辨哪些是可回收垃圾，哪些是不可回收垃圾的助手，请你分析用户告诉你的信息，然后告诉用户哪些是可回收垃圾哪些是不可回收垃圾");
        prompt.add(message);
        return prompt;
    }

    public static  JSONArray aiprompt() {
        JSONArray prompt = new JSONArray();
        Message message = new Message("system","你是聊天内容的审查ai，请你判断对话内容是否和垃圾分类有关，你的输出只有是或否");
        prompt.add(message);
        return prompt;
    }
}


