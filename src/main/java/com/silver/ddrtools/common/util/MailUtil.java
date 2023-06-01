package com.silver.ddrtools.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName mailUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/7/17 14:20
 * @Version 1.0
 **/
@Component
public class MailUtil {

    @Autowired
    JavaMailSender javaMailSender;

    public void endRegisterMail(String username,String token,String senEemail) throws Exception {
        MimeMessage mailMessage=javaMailSender.createMimeMessage();
        //需要借助Helper类
        MimeMessageHelper helper=new MimeMessageHelper(mailMessage);
        String context="<b>尊敬的用户：</b><br>"+username+"您好，欢迎注册ddrtools，"+
                "请您尽快通过<a href=\"http://localhost:8088/um_userinfo/activate?token="+token+"\">链接</a>激活账号。";
        try {
            helper.setFrom("403896948@qq.com");
            helper.setTo(senEemail);
//            helper.setBcc("密送人");
            helper.setSubject("激活账号");
            helper.setSentDate(new Date());//发送时间
            helper.setText(context,true);
            //第一个参数要发送的内容，第二个参数是不是Html格式。


            javaMailSender.send(mailMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
