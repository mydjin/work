package com.qihsoft.webdev.utils.tool;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.utils.convert.V;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

public class wechatSMSUtil {
    public static final String API_URL = "http://yb.gxun.edu.cn/comsumer";


    public static void main(String[] args) throws Exception {
        send("17677256676","验证码：fhzjvgdzhjfgshfgjhfghj");
        getSMS();




    }



    //直接发送短信：true为成功，false为失败(默认调用此方法)
    public static boolean send(String phonecode, String Content) {
        try {
            String result = sendPost(API_URL+"/sms/send","content="+Content+"&phonecode="+phonecode);

            JSONObject obj = JSON.parseObject(result);

            if(!V.isEmpty(obj)){
                JSONObject res = obj.getJSONObject("LANZ_ROOT");
                if (res.getString("ErrorNum").equals("0")) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    //直接发送短信：true为成功，false为失败(默认调用此方法)
    public static JSONObject sendObj(String phonecode, String Content) {
        try {
            String result = sendPost(API_URL+"/sms/send","content="+Content+"&phonecode="+phonecode);

           JSONObject obj = JSON.parseObject(result);

            if(!V.isEmpty(obj)){
                JSONObject res = obj.getJSONObject("LANZ_ROOT");
                return  res;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



    //直接获取上行短信
    public static JSONObject getSMS() {
        try {
            String result = sendPost(API_URL+"/sms/getsms","");

            JSONObject obj =JSON.parseObject(result);
            if(!V.isEmpty(obj)){
                JSONObject res = obj.getJSONObject("LANZ_ROOT");
              return  res;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }






    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        String auth="wxuser:8#06gxun001%";

        byte[] encodedAuth = Base64.getEncoder()
                .encode(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn= (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            if(conn.getResponseCode()==200) {

                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }else{
                return null;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }





}