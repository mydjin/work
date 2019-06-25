package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.CustomMapper;
import com.xtaller.party.core.model.Message;
import com.xtaller.party.core.model.Note;
import com.xtaller.party.core.model.SysOperationRecord;
import com.xtaller.party.core.service.ICustomService;
import com.xtaller.party.utils.tool.WeixinPushUtil;
import com.xtaller.party.core.tps.CacheKit;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.J;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.kit.IdKit;
import com.xtaller.party.utils.kit.TimeKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Taller on 2017/11/08
 */
@Service
public class CustomService extends TServiceImpl<CustomMapper, JSONObject> implements ICustomService {


    public JSONObject getUserById(String id) {

        return F.f2j(baseMapper.getUserById(id), "id");
    }


    public JSONObject getUserByNumber(String number) {

        return F.f2j(baseMapper.getUserByNumber(number), "id");
    }


    public JSONObject getOpenIdByIdAndType(String userid, String type) {

        return F.f2j(baseMapper.getOpenIdByIdAndType(userid, type), "openId");
    }

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
    public boolean sendMessage(Message sendMessage, String token) {
        Message message = o2c(sendMessage, token, Message.class);
        JSONObject values = customService.getUserByNumber(message.getNumber());
        if (!V.isEmpty(values)) {
            message.setName(values.getString("name"));

            JSONObject number = values;
            String str = message.getContent();
            //根据标签替换模板
            Pattern pattern = Pattern.compile("#\\{.*?}");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String regex = matcher.group(0);
                String tag = regex.substring(regex.indexOf("#{") + 2, regex.indexOf("}"));
                if (number.getString(tag) != null) {
                    str = str.replace(regex, number.getString(tag));
                } else {
                    str = str.replace(regex, "");
                }
            }
            message.setContent(str);

            message.setSendTime((int) TimeKit.getTimestamp());
            Message model = messageService.createMessage(message);
            if (model == null) {
                return false;
            } else {
                return true;
            }


        } else {
            return false;
        }

    }

    //批量发送消息
    public int sendMessages(List<Message> sendMessages, String token) {
        String userId = cacheKit.getUserId(token);
        String values = "";
        List<Message> Messages = new ArrayList<>();
        for (int i = 0; i < sendMessages.size(); i++) {
            Message message = o2c(sendMessages.get(i), token, Message.class);
            JSONObject numbers = customService.getUserByNumber(message.getNumber());
            if (!V.isEmpty(numbers)) {
                JSONObject number = numbers;
                String str = message.getContent();
                //根据标签替换模板
                Pattern pattern = Pattern.compile("#\\{.*?}");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    String regex = matcher.group(0);
                    String tag = regex.substring(regex.indexOf("#{") + 2, regex.indexOf("}"));
                    if (number.getString(tag) != null) {
                        str = str.replace(regex, number.getString(tag));
                    } else {
                        str = str.replace(regex, "");
                    }
                }


                message.setName(number.getString("name"));
                message.setContent(str);
                message.setCreator(userId);
                message.setSendTime((int) TimeKit.getTimestamp());
                Messages.add(message);

            }

        }
        int result = messageService.sendMessages(Messages);
        return result;
    }

    //微信推送 传入参数
    public boolean sendWeixinPush(String number, String title, String content, String url, String type) {

        JSONObject info = customService.getUserByNumber(number);
        if (!V.isEmpty(info)) {
            JSONObject openIdObj = customService.getOpenIdByIdAndType(info.getString("id"), "weixin");
            if (!V.isEmpty(openIdObj)) {
                String str = content;
                //根据标签替换模板
                Pattern pattern = Pattern.compile("#\\{.*?}");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    String regex = matcher.group(0);
                    String tag = regex.substring(regex.indexOf("#{") + 2, regex.indexOf("}"));
                    if (info.getString(tag) != null) {
                        str = str.replace(regex, info.getString(tag));
                    } else {
                        str = str.replace(regex, "");
                    }
                }
                String openid = openIdObj.getString("openId");
                if (WeixinPushUtil.sendPush(openid, title, str, url, type)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Autowired
    private NoteService noteService;

    //短信发送 传入参数
    public boolean sendNote(String number, String theme, String detail, Integer type, String token) {

        Note note = o2c(new Note(), token, Note.class);
        JSONObject values = customService.getUserByNumber(number);
        if (!V.isEmpty(values)) {
            if (!V.isEmpty(values.getString("phone"))) {
                note.setPhone(values.getString("phone"));
                note.setName(values.getString("name"));
                note.setTheme(theme);
                note.setNumber(number);
                JSONObject info = values;
                String str = detail;
                //根据标签替换模板
                Pattern pattern = Pattern.compile("#\\{.*?}");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    String regex = matcher.group(0);
                    String tag = regex.substring(regex.indexOf("#{") + 2, regex.indexOf("}"));
                    if (info.getString(tag) != null) {
                        str = str.replace(regex, info.getString(tag));
                    } else {
                        str = str.replace(regex, "");
                    }
                }
                note.setDetail(str + "(智慧党建)");
                note.setType(type);
                note.setSendTime((int) TimeKit.getTimestamp());
                boolean res = noteService.SendNote(note);
                return res;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    //短信发送 传入对象
    public boolean sendNote(Note sendNote, String token) {

        Note note = o2c(sendNote, token, Note.class);
        JSONObject values = customService.getUserByNumber(sendNote.getNumber());
        if (!V.isEmpty(values)) {
            if (!V.isEmpty(values.getString("phone"))) {
                note.setName(values.getString("name"));
                note.setPhone(values.getString("phone"));
                JSONObject info = values;
                String str = note.getDetail();
                //根据标签替换模板
                Pattern pattern = Pattern.compile("#\\{.*?}");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    String regex = matcher.group(0);
                    String tag = regex.substring(regex.indexOf("#{") + 2, regex.indexOf("}"));
                    if (info.getString(tag) != null) {
                        str = str.replace(regex, info.getString(tag));
                    } else {
                        str = str.replace(regex, "");
                    }
                }
                note.setDetail(str + "(智慧党建)");
                note.setSendTime((int) TimeKit.getTimestamp());
                boolean res = noteService.SendNote(note);
                return res;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }


    @Autowired
    private AcademyInfoService academyService;

    //根据学院代码获取用户列表
    public List<JSONObject> queryUserObjByAcademyCode(String academyCode) {

        List<JSONObject> list = academyService.queryUserByAcademyCode(academyCode);

        return F.f2l(list, "number");
    }

    //根据学院代码获取用户串
    public String queryUserNumbersByAcademyCode(String academyCode) {
        List<JSONObject> list = academyService.queryUserByAcademyCode(academyCode);
        if (V.isEmpty(list)) {
            return null;
        }
        String ids = "";
        for (int i = 0; i < list.size(); i++) {
            ids += "'" + list.get(i).getString("number") + "'";
            if (i + 1 != list.size()) {
                ids += ",";
            }
        }
        return ids;
    }

    //根据学院代码判断用户是否有权限
    public boolean checkUserByAcademyCode(String academyCode, String number) {

        boolean flag = academyService.checkUserByAcademyCode(academyCode, number);
        return flag;
    }

    @Autowired
    private LinkUserService linkUserService;

    //根据联络人工号查用户列表
    public List<JSONObject> queryUserObjByLinkNumber(String linkNumber) {
        List<JSONObject> list = linkUserService.queryNumberObjByLinkNumber(linkNumber);
        return F.f2l(list, "id", "creator", "reverse");
    }

    //根据联络人工号查用户串
    public String queryUserNumbersByLinkNumber(String linkNumber) {
        List<JSONObject> list = linkUserService.queryNumberObjByLinkNumber(linkNumber);
        if (V.isEmpty(list)) {
            return null;
        }
        String ids = "";
        for (int i = 0; i < list.size(); i++) {
            ids += "'" + list.get(i).getString("number") + "'";
            if (i + 1 != list.size()) {
                ids += ",";
            }
        }
        return ids;
    }

    //根据联络人工号判断用户是否有权限
    public boolean checkUserByLinkNumber(String linkNumber, String number) {

        boolean flag = linkUserService.checkUserByLinkNumber(linkNumber, number);
        return flag;
    }


}