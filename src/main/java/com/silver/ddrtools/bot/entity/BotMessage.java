package com.silver.ddrtools.bot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName BotMessage
 * @Description TODO
 * @Author silver
 * @Date 2022/11/16 10:42
 * @Version 1.0
 **/

@NoArgsConstructor
@Data
public class BotMessage {
    //群聊
    public static final String  GROUP = "group";
    //私聊
    public static final String  PRIVATE = "private";



    /**
     * raw_message : /卧底 _(:з」∠)_
     * self_id : 767992327
     * message_type : group
     * message_id : -727963516
     * message : /卧底 _(:з」∠)_
     * sub_type : normal
     * group_id : 532599989
     * sender : {"area":"","role":"member","level":"","user_id":694097209,"sex":"unknown","nickname":"Tsui","title":"","age":0,"card":"小可爱"}
     * user_id : 694097209
     * post_type : message
     * time : 1668523031
     * message_seq : 1976
     * font : 0
     */

    private String raw_message;
    private String self_id;
    private String message_type;
    private String message_id;
    private String message;
    private String sub_type;
    private String group_id;
    private SenderBean sender;
    private String user_id;
    private String post_type;
    private String time;
    private String message_seq;
    private String font;

    @NoArgsConstructor
    @Data
    public static class SenderBean {
        /**
         * area :
         * role : member
         * level :
         * user_id : 694097209
         * sex : unknown
         * nickname : Tsui
         * title :
         * age : 0
         * card : 小可爱
         */

        private String area;
        private String role;
        private String level;
        private String user_id;
        private String sex;
        private String nickname;
        private String title;
        private String age;
        private String card;
    }
}
