package com.silver.ddrtools.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.silver.ddrtools.common.entity.*;
import com.silver.ddrtools.common.manager.MapinfoManager;
import com.silver.ddrtools.common.manager.UserJsonManager;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.service.impl.SysSettingServiceImpl;

import net.dreamlu.mica.core.spring.SpringContextUtil;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;

/**
 * @ClassName HttpRequestUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/11/3 13:27
 * @Version 1.0
 **/
public class HttpRequestUtil {

    public static String doGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("DataEncoding", "UTF-8");
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                return null;
            }
            return EntityUtils.toString(entity, "utf-8");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * @Description: 发送get请求
     */
    public static String doddrGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("DataEncoding", "UTF-8");

        RequestConfig requestConfig = null;

//        if (SystemRunTimeParam.systemName.equals("linux")) {
//            HttpHost proxy = new HttpHost("127.0.0.1",7890);
//            requestConfig = RequestConfig.custom().setConnectTimeout(35000).setProxy(proxy).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        }else {
            requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        }
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                return null;
            }
            return EntityUtils.toString(entity, "utf-8");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @Description: 发送http post请求
     */
    public static String doPost(String url, String jsonStr) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("DataEncoding", "UTF-8");
        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                return null;
            }
            HttpEntity entity = (HttpEntity) httpResponse.getEntity();
            String result = EntityUtils.toString((org.apache.http.HttpEntity) entity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * @Description: 发送get请求
     */
    public static String queryDDrGet(String DDrjsonurl,String queryStr,boolean mapflag) {
        String url = null;
        try {
            if (queryStr!=null){
                if (mapflag) {
                    url = DDrjsonurl + queryStr;
                }else {
                    url = DDrjsonurl + URLEncoder.encode(queryStr, "UTF-8");
                }
            }else {
                url = DDrjsonurl;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String result ;
        String finalUrl = url;
        if (StringUtil.isBlank(queryStr)){
            queryStr = "null";
        }
        if (mapflag){
            result = MapinfoManager.getInstance().getMap().get(queryStr, key -> doddrGet(finalUrl));
        }else {
            result = UserJsonManager.getInstance().getMap().get(queryStr, key-> doddrGet(finalUrl));
        }
        return StringUtil.isBlank(result) ? "error" : result;
    }



    public static String tts( String content,String filepath,String voice) throws Exception {
        String url = "http://124.71.9.179:3000/api/ra";
        // 创建 URL 对象
        URL obj = new URL(url);
        //把content进行url编码
        if (voice==null){
            voice = "zh-CN-XiaoyiNeural";
        }
        String urlContent = URLEncoder.encode(content, "UTF-8");
        String body = "<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"https://www.w3.org/2001/mstts\" xml:lang=\"en-US\">\n" +
                "  <voice name=\""+voice+"\">\n" +
                "    "+content+"\n" +
                "  </voice>\n" +
                "</speak>";
        // 创建 HttpURLConnection 对象
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // 设置请求方法
        con.setRequestMethod("POST");
        // 设置请求头
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("FORMAT", "webm-24khz-16bit-mono-opus");
        // 设置请求正文
        con.setDoOutput(true);
        con.getOutputStream().write(body.getBytes("UTF-8"));

        // 获取响应结果
        InputStream inputStream = con.getInputStream();
        //写入到磁盘上
        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, len);
        }
        fileOutputStream.close();
        inputStream.close();
        // 返回响应结果
        return null;
    }
    public static Boolean chattoGPT( ChatBotBean chatbot)  {
        String url = "https://api.chatanywhere.com.cn/v1/chat/completions";
        // 创建 URL 对象
        URL obj = null;
        try {
            obj = new URL(url);

        JSONArray messages = new JSONArray();
        messages.addAll(chatbot.getContext());
        RequestBody requestBody = new RequestBody("gpt-3.5-turbo", 0.7, messages);
        String body = JSON.toJSONString(requestBody);


        // 创建 HttpURLConnection 对象
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // 设置请求方法
        con.setRequestMethod("POST");
        // 设置请求头
        con.setRequestProperty("Authorization", "Bearer "+ChatGptUtil.apikey);
        con.setRequestProperty("Content-Type", "application/json");
        // 设置请求正文
        con.setDoOutput(true);
        con.getOutputStream().write(body.getBytes("UTF-8"));

        // 获取响应结果
        InputStream inputStream = con.getInputStream();
        //从输入流中读取响应，打印
        StringBuilder sb = new StringBuilder("");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        bufferedReader.lines().forEach(sb::append);
        String result = sb.toString();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("choices");
        JSONArray context = chatbot.getContext();
        context.add(jsonArray.getJSONObject(0).getJSONObject("message"));
        System.out.println(jsonArray.getJSONObject(0).getJSONObject("message"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static Boolean regulation ( JSONArray prompt,String content)  {
        String url = "https://api.chatanywhere.com.cn/v1/chat/completions";
        // 创建 URL 对象
        URL obj = null;
        try {
            obj = new URL(url);

            JSONArray messages = new JSONArray();
            messages.addAll(prompt);
            Message message = new Message("user",content);
            messages.add(message);
            RequestBody requestBody = new RequestBody("gpt-3.5-turbo", 0.7, messages);
            String body = JSON.toJSONString(requestBody);


            // 创建 HttpURLConnection 对象
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 设置请求方法
            con.setRequestMethod("POST");
            // 设置请求头
            con.setRequestProperty("Authorization", "Bearer "+ChatGptUtil.apikey);
            con.setRequestProperty("Content-Type", "application/json");
            // 设置请求正文
            con.setDoOutput(true);
            con.getOutputStream().write(body.getBytes("UTF-8"));

            // 获取响应结果
            InputStream inputStream = con.getInputStream();
            //从输入流中读取响应，打印
            StringBuilder sb = new StringBuilder("");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            bufferedReader.lines().forEach(sb::append);
            String result = sb.toString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("choices");
            JSONObject message1 = jsonArray.getJSONObject(0).getJSONObject("message");
            String text = message1.getString("content");
            System.out.println(text);
            if ("是".equals(text)){
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String createImage ( String content)  {
        String url = "https://api.chatanywhere.com.cn/v1/images/generations";
        // 创建 URL 对象
        URL obj = null;
        try {
            obj = new URL(url);


            String body = "{\n" +
                    "  \"prompt\": \""+content+"\",\n" +
                    "  \"n\": 1,\n" +
                    "  \"size\": \"1024x1024\"\n" +
                    "}";
            // 创建 HttpURLConnection 对象
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 设置请求方法
            con.setRequestMethod("POST");
            // 设置请求头
            con.setRequestProperty("Authorization", "Bearer "+ChatGptUtil.apikey);
            con.setRequestProperty("Content-Type", "application/json");
            // 设置请求正文
            con.setDoOutput(true);
            con.getOutputStream().write(body.getBytes("UTF-8"));

            // 获取响应结果
            InputStream inputStream = con.getInputStream();
            //从输入流中读取响应，打印
            StringBuilder sb = new StringBuilder("");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            bufferedReader.lines().forEach(sb::append);
            String result = sb.toString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject message1 = jsonArray.getJSONObject(0);
            String imgurl = message1.getString("url");

            String localimgurl = FileUtil.saveImage(imgurl, "/home/ddrtools/chatimg/");
            System.out.println(localimgurl);
            return localimgurl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private  void test (){
        //从url通过二进制溜下载图片到本体
    }

}
