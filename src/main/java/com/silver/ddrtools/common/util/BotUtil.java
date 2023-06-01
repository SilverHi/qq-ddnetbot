package com.silver.ddrtools.common.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName BotUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/11/3 13:54
 * @Version 1.0
 **/

public class BotUtil {
    public static String GROUP = "group";
    public static String PRIVATE = "private";

    public static void sendPrivate(String userId, String message) {
        try {
            HttpRequestUtil.doGet("http://127.0.0.1:5700/send_private_msg?user_id=" + userId + "&message=" + URLEncoder.encode(message, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGroup(String groupId, String message) {
        try {
            HttpRequestUtil.doGet("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + URLEncoder.encode(message, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void autoSwitchSend(String type,String groupId,String userId, String message) {
        if (type.equals(PRIVATE)) {
            sendPrivate(userId, message);
        } else if (type.equals(GROUP)) {
            sendGroup(groupId, message);
        }
    }

    public static void sendGroupTempPrivate(String Code,String groupid, String message) {
        try {
            HttpRequestUtil.doGet("http://127.0.0.1:5700/send_private_msg?user_id=" + Code + "&group_id="+groupid+"&message=" + URLEncoder.encode(message, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改群头衔
    public static void setGroupSpecialCard(String groupId, String userId, String card) {
        try {
            HttpRequestUtil.doGet("http://127.0.0.1:5700/set_group_special_title?user_id=" + userId + "&group_id="+groupId+"&special_title=" + URLEncoder.encode(card, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //修改群名片
    public static void setGroupCard(String groupId, String userId, String card) {
        try {
            HttpRequestUtil.doGet("http://127.0.0.1:5700/set_group_card?user_id=" + userId + "&group_id="+groupId+"&card=" + URLEncoder.encode(card, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static JSONObject getJSONParam(HttpServletRequest request) {
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 数据写入Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }

    public static boolean isNumeric(String userInput) {
        for (int i = userInput.length(); --i >= 0; ) {
            if (!Character.isDigit(userInput.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}


