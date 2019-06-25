package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.ICustomService;
import com.qihsoft.webdev.core.mapper.CustomMapper;
import com.qihsoft.webdev.core.model.Message;
import com.qihsoft.webdev.core.model.Note;
import com.qihsoft.webdev.core.tps.CacheKit;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.J;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.kit.IdKit;
import com.qihsoft.webdev.utils.kit.TimeKit;
import com.qihsoft.webdev.utils.tool.HttpTookit;
import com.qihsoft.webdev.utils.tool.PropertiesUtil;
import com.qihsoft.webdev.utils.tool.WeixinPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Qihsoft on 2017/11/08
 */
@Service
public class CustomService extends TServiceImpl<CustomMapper,JSONObject> implements ICustomService {






    /**
     * 对象转换
     */
    public <T> T o2c(Object o, String token, Class cls) {
        JSONObject object = J.o2j(o);
        // 修改人
        object.put("creator", cacheKit.user(token).get("id"));
        return o2t(object, (Class<T>) cls);
    }

    public <T> T o2t(Object o, Class cls) {
        JSONObject object = J.o2j(o);
        //字符串数据处理
        Map<String, Object> map = J.o2m(o);
        Map.Entry entry;
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            String val = entry.getValue().toString().trim();
            if (val.indexOf("'") >= 0)
                val = val.replaceAll("'", "\'");
            if (val.indexOf("\"") >= 0)
                val = val.replaceAll("\"", "\"");
            object.put(entry.getKey().toString(), val);
        }
        if (object.get("id") == null)
            object.put("id", IdKit.getId(cls));
        return J.o2m(object, (Class<T>) cls);
    }

    @Autowired
    public CacheKit cacheKit;
    @Autowired
    private CustomService customService;
    @Autowired
    private MessageService messageService;

    //发送消息：传入对象
    public boolean sendMessage(Message sendMessage, String token){
        Message  message= o2c(sendMessage,token,Message.class);
        JSONObject values = new JSONObject();
            values =   customService.getMssageUserByNumber(message.getNumber());
        if( V.isEmpty(values)) {
                values = customService.getWechatMessageUserByNumber(message.getNumber());
            }

        if( !V.isEmpty(values)) {
            message.setName(values.getString("name"));

            JSONObject number = values;
            String str =message.getContent();
            //根据标签替换模板
            Pattern pattern = Pattern.compile("#\\{.*?}");
            Matcher matcher = pattern.matcher(str);
            while(matcher.find()){
                String regex=matcher.group(0);
                String tag = regex.substring(regex.indexOf("#{")+2,regex.indexOf("}"));
                if(number.getString(tag)!=null){
                    str= str.replace(regex,number.getString(tag));
                }else{
                    str= str.replace(regex,"");
                }
            }
            message.setContent(str);
            if(V.isEmpty(message.getSendStatus())){
                message.setSendStatus(1);
            }
            if(V.isEmpty( message.getSendTime())){
                message.setSendTime((int) TimeKit.getTimestamp());
            }
            Message  model=     messageService.createMessage(message);
            if(model==null){
                return  false;
            }else{
                return  true;
            }


        }else{
            return  false;
        }

    }

    //批量发送消息
    public int sendMessages(List<Message>  sendMessages,String token){
        String userId = cacheKit.getUserId(token);
        List<Message>  Messages =new ArrayList<>();
        System.out.println(JSONObject.toJSONString(Messages));

        for (int i=0;i<sendMessages.size();i++) {
            Message message = o2c(sendMessages.get(i), token, Message.class);
            JSONObject numbers = new JSONObject();
                numbers =   customService.getMssageUserByNumber(message.getNumber());
            if( V.isEmpty(numbers)) {
                numbers = customService.getWechatMessageUserByNumber(message.getNumber());
            }
            if(! V.isEmpty(numbers)) {
                JSONObject number = numbers ;
                String str =message.getContent();
                //根据标签替换模板
                Pattern pattern = Pattern.compile("#\\{.*?}");
                Matcher matcher = pattern.matcher(str);
                while(matcher.find()){
                    String regex=matcher.group(0);
                    String tag = regex.substring(regex.indexOf("#{")+2,regex.indexOf("}"));
                    if(number.getString(tag)!=null){
                        str= str.replace(regex,number.getString(tag));
                    }else{
                       str= str.replace(regex,"");
                    }
                }
                message.setName(number.getString("name"));
                message.setContent(str);
                message.setCreator(userId);
                if(V.isEmpty(message.getSendStatus())){
                    message.setSendStatus(1);
                }
                if(V.isEmpty( message.getSendTime())){
                    message.setSendTime((int) TimeKit.getTimestamp());
                }
                Messages.add(message);

            }

        }
       int result =   messageService.sendMessage(Messages);
       return  result;
    }


    @Autowired
    private NoteService noteService;

    //短信发送 传入参数
    public boolean sendNote(String phone,String number,String theme,String detail,Integer type,String token){

       Note note= o2c(new Note(),token,Note.class);
        JSONObject values = new JSONObject();
            values =   customService.getMssageUserByNumber(number);
        if( V.isEmpty(values)) {
                values = customService.getWechatMessageUserByNumber(number);
            }


        if( !V.isEmpty(values)) {

            note.setName(values.getString("name"));
            note.setTheme(theme);
            note.setNumber(number);
            JSONObject info = values ;
            String str =detail;
            //根据标签替换模板
            Pattern pattern = Pattern.compile("#\\{.*?}");
            Matcher matcher = pattern.matcher(str);
            while(matcher.find()){
                String regex=matcher.group(0);
                String tag = regex.substring(regex.indexOf("#{")+2,regex.indexOf("}"));
                if(info.getString(tag)!=null){
                    str= str.replace(regex,info.getString(tag));
                }else{
                    str= str.replace(regex,"");
                }
            }
            note.setDetail(str+"（由系统快速开发平台应用发送，请勿回复）");
            note.setPhone(phone);
            note.setType(type);
            note.setSendTime((int) TimeKit.getTimestamp());
           boolean res= noteService.SendNote(note);
           return res;
        }else{
            return  false;
        }

    }

    //短信发送 传入对象
    public boolean sendNote(Note sendNote,String token){

        Note note= o2c(sendNote,token,Note.class);
        JSONObject values = new JSONObject();
            values =   customService.getMssageUserByNumber(sendNote.getNumber());
        if( V.isEmpty(values)) {
            values = customService.getWechatMessageUserByNumber(sendNote.getNumber());
        }


        if( !V.isEmpty(values)) {
            note.setName(values.getString("name"));
            JSONObject info = values;
            String str =note.getDetail();
            //根据标签替换模板
            Pattern pattern = Pattern.compile("#\\{.*?}");
            Matcher matcher = pattern.matcher(str);
            while(matcher.find()){
                String regex=matcher.group(0);
                String tag = regex.substring(regex.indexOf("#{")+2,regex.indexOf("}"));
                if(info.getString(tag)!=null){
                    str= str.replace(regex,info.getString(tag));
                }else{
                    str= str.replace(regex,"");
                }
            }
            note.setDetail(str+"（由系统快速开发平台应用发送，请勿回复）");
            note.setSendTime((int) TimeKit.getTimestamp());
            boolean res= noteService.SendNote(note);
            return res;
        }else{
            return  false;
        }

    }

    public JSONObject getMssageUserByNumber(String number){
        

        return F.f2j(baseMapper.getMssageUserByNumber(number),"id");
    }

    public JSONObject getWechatMessageUserByNumber(String number){

        return F.f2j(baseMapper.getWechatMessageUserByNumber(number),"id");
    }


    public JSONObject  getTokenByIdAndType(String userid,String type){

        return F.f2j(baseMapper. getTokenByIdAndType(userid,type),"openId","id");
    }

    public JSONObject queryUserIdByToken(String type,String token) {
        JSONObject obj = baseMapper.queryUserIdByToken(type,token);
        return F.f2j(obj,"id","userId");
    }


    //微信推送 传入对象  number 学号 pushUrl推送地址 pushType推送类别 pushData推送数据
    //推送类别 pushType： login 登录 bind 绑定  bookreturn图书催还
    // obj 推送数据:
    // login 登录 需要字段 wechatName 微信名 number 学号
    // bind 绑定 需要字段 wechatName 微信名 number 学号 myClass班级  name姓名
    // teabind 教师绑定 需要字段 wechatName 微信名 number 工号 name姓名
    // bookreturn图书催还 需要字段 returnDate归还时间  bookName图书名称
    // bookreturnout图书逾期提醒 需要字段 returnDate归还时间  bookName图书名称
    // courseTips上课提醒 需要字段 course课程 time时间  place地点  tipsType:今日/明日
    // cardTips一卡通余额提醒 需要字段 balance 余额 number 学工号 name姓名

    public boolean sendWeixinPush(JSONObject obj){

        //System.out.println("传入对象："+obj);
        PropertiesUtil conf= new PropertiesUtil("conf.properties");

        String version = conf.readProperty("weixinweb.version");


        if(V.isEmpty(version)){
            version= S.getToken();
        }



        if(obj==null){
            return  false;
        }

        if(obj.getString("number")==null||obj.getString("pushUrl")==null||obj.getString("pushType")==null||obj.getJSONObject("pushData")==null){
            return  false;
        }

        String number=obj.getString("number");
        String url=obj.getString("pushUrl")+"?version="+version;
        String type=obj.getString("pushType");
        JSONObject data=obj.getJSONObject("pushData");
        data.put("number",obj.getString("number"));

        JSONObject openIdObj =new JSONObject();
        if(!V.isEmpty(cacheKit.getVal("weixin_" + number + "_openId"))) {
            openIdObj = JSONObject.parseObject(cacheKit.getVal("weixin_" + number + "_openId"));

        }else {
             openIdObj = customService.getTokenByIdAndType(number, "weixin");
            if (!V.isEmpty(openIdObj)) {
                if (!V.isEmpty( openIdObj.getString("openId"))) {
                    cacheKit.setVal("weixin_" + number + "_openId", openIdObj.toJSONString(), 0);
                }
            }
        }
            if (!V.isEmpty(openIdObj)) {
                String weixinToken =getWeixinAccessToken();

                String openid =openIdObj.getString("openId");
                if(WeixinPushUtil.sendPush(openid,url,data,type,weixinToken)){
                    return true;
                }else{
                    return  false;
                }
            }else{
                return  false;
            }

    }

    public String getWeixinAccessToken() {

        String access_token ="" ;
        if(V.isEmpty(cacheKit.getVal("weixin_access_token"))||!V.isEmpty(cacheKit.getVal("weixin_user_access_token"))){
            if(!V.isEmpty(cacheKit.getVal("weixin_user_access_token"))){
                cacheKit.deleteVal("weixin_user_access_token");
            }
            PropertiesUtil db= new PropertiesUtil("conf.properties");
            String weixin_appId = db.readProperty("weixin.appId");
            String weixin_appsecret = db.readProperty("weixin.appsecret");
            String tokenStr = HttpTookit.sendPost("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + weixin_appId + "&secret=" + weixin_appsecret);
            JSONObject tokenData=JSONObject.parseObject(tokenStr);
            //appid和secret从微信后台获取
            access_token = tokenData.getString("access_token");

            if ( access_token != null&& access_token !="") {
                int expires = tokenData.getInteger("expires_in");
                cacheKit.setVal("weixin_access_token", access_token, expires);
            }else{
            //    System.out.println(tokenStr);

            }

        }else {
            access_token =cacheKit.getVal("weixin_access_token");
        }


        String ipListStr = HttpTookit.sendGet("https://api.weixin.qq.com/cgi-bin/getcallbackip", "access_token="+access_token );
        JSONObject ipListJson = JSON.parseObject(ipListStr);
        JSONArray ipList = ipListJson .getJSONArray("ip_list");
        if (ipList!= null) {
            cacheKit.setVal("weixin_ip_list",ipList.toJSONString(), 0);
         //   System.out.println(ipList.toJSONString());

        }else{
             access_token =  syncWeixinAccessToken();
        }
        if(!V.isEmpty(access_token)&&access_token!=""){
            return  access_token;
        }else{
            return  null;
        }

    }

    public String syncWeixinAccessToken() {

        String access_token ="" ;
         cacheKit.deleteVal("weixin_user_access_token");
        cacheKit.deleteVal("weixin_access_token");

        PropertiesUtil db= new PropertiesUtil("conf.properties");
            String weixin_appId = db.readProperty("weixin.appId");
            String weixin_appsecret = db.readProperty("weixin.appsecret");
        String tokenStr = HttpTookit.sendPost("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + weixin_appId + "&secret=" + weixin_appsecret);
        JSONObject tokenData=JSONObject.parseObject(tokenStr);
        //appid和secret从微信后台获取
        access_token = tokenData.getString("access_token");

        if ( access_token != null&& access_token !="") {
            int expires = tokenData.getInteger("expires_in");
            cacheKit.setVal("weixin_access_token", access_token, expires);
        }else{
            System.out.println(tokenStr);

        }


        /*String ipListStr = HttpTookit.sendGet("https://api.weixin.qq.com/cgi-bin/getcallbackip", "access_token="+access_token );
        JSONObject ipListJson = JSON.parseObject(ipListStr);
        JSONArray ipList = ipListJson .getJSONArray("ip_list");
        if (ipList!= null) {
            cacheKit.setVal("weixin_ip_list",ipList.toJSONString(), 0);
         //   System.out.println(ipList.toJSONString());
        }else{
            access_token =  syncWeixinAccessToken();
        }*/

        if(!V.isEmpty(access_token)&&access_token!=""){
            return  access_token;
        }else{
            return  null;
        }

    }




    }