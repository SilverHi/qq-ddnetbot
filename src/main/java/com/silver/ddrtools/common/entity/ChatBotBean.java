package com.silver.ddrtools.common.entity;

import com.alibaba.fastjson.JSONArray;
import com.github.plexpt.chatgpt.Chatbot;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ChatBotBean
 * @Description TODO
 * @Author silver
 * @Date 2022/12/8 13:42
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ChatBotBean {
    private String user_id;
    //是否已回复
    private Boolean isReply;
    private JSONArray context;

}
