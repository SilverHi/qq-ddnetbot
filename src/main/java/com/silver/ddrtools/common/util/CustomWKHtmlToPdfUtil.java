package com.silver.ddrtools.common.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.manager.UserInfoManager;

import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CustomWKHtmlToPdfUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 17:04
 * @Version 1.0
 **/
@Slf4j
public class CustomWKHtmlToPdfUtil {


    public static String getCommand(String param,String sourceFilePath, String targetFilePath) {

        String system = System.getProperty("os.name");
        if (system.contains("Windows")) {
            return "C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltoimage.exe "+param+" " + sourceFilePath + " " + targetFilePath;
        } else {
            return "wkhtmltoimage " +param+""+ sourceFilePath + " " + targetFilePath;
        }
    }


    public static void html2img(String htmlPath,String imgPath) throws Exception {
        CustomWKHtmlToPdfUtil util = new CustomWKHtmlToPdfUtil();
        String command = util.getCommand(" --enable-local-file-access  ",htmlPath, imgPath);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); //这个调用比较关键，就是等当前命令执行完成后再往下执行
        log.info("执行转换完成");
    }

    public static void html2imgWithParam(String param ,String htmlPath,String imgPath) throws InterruptedException, IOException {
        CustomWKHtmlToPdfUtil util = new CustomWKHtmlToPdfUtil();
        String command = util.getCommand(" --enable-local-file-access  "+param,htmlPath, imgPath);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); //这个调用比较关键，就是等当前命令执行完成后再往下执行
        log.info("执行转换完成");
    }

    public static void writeHtml(String htmlPath,String html) throws IOException {
        try {
            FileWriter fw = new FileWriter(htmlPath);
            fw.write(html);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHtmlFromUrl(String DDrurl,String playerName) {
        String url = DDrurl + playerName +"/" ;
// 新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        //如果是linux系统，需要设置代理
        String htmlStr = UserInfoManager.getInstance().getMap().get(playerName,key->{
            WebClient webClient = null;
//            if (SystemRunTimeParam.systemName.equals("linux")) {
//                webClient= new WebClient(BrowserVersion.CHROME,"127.0.0.1",7890);
//            }else {
                webClient = new WebClient(BrowserVersion.CHROME);
//            }

            // 当JS执行出错的时候是否抛出异常, 这里选择不需要
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // 当HTTP的状态非200时是否抛出异常, 这里选择不需要
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setActiveXNative(false);
            // 是否启用CSS, 因为不需要展现页面, 所以不需要启用
            webClient.getOptions().setCssEnabled(true);
            // 启用JS
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setUseInsecureSSL(true);
            // 很重要，设置支持AJAX
            webClient.setAjaxController(
                    new NicelyResynchronizingAjaxController());

            HtmlPage page = null;
            try {
                page = webClient.getPage(url);
            } catch (Exception ignored) {
            }finally {
                webClient.close();
            }
            if (page != null) {
                return page.asXml();
            }
            return null;
        });
        return htmlStr==null?"error":htmlStr;
    }

}
