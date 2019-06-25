package com.qihsoft.webdev.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.tps.CacheKit;
import com.qihsoft.webdev.core.service.impl.CustomService;
import com.qihsoft.webdev.utils.base.TApi;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.J;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.kit.IdKit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Qihsoft
 * @create 2017-12-01 10:54
 */
public class BaseApi extends TApi {
    @Autowired
    public CacheKit cacheKit;
    @Autowired
    public CustomService customService;

    public int adminServer = 0;

    public int wechatServer = 1;


    /**
     * 对象转换
     */
    public <T> T o2c(Object o, String token, Class cls) {
        JSONObject object = J.o2j(o);
        // 修改人
        object.put("creator", cacheKit.user(token).get("id"));
        return o2t(object, (Class<T>) cls);
    }

    public <T> T o2u(Object o, String token, Class cls) {
        JSONObject object = J.o2j(o);
        // 修改人
        object.put("reviser", cacheKit.user(token).get("id"));
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

    /**
     * 后台用户缓存
     *
     * @param
     */
    public Boolean initCacheUser(JSONObject user, String token, int SESSION_TIME) {
        String uid = F.s("ump-admin-%s-user", user.getString("id"));
        //判断是否存在uid数据
        String oldToken = cacheKit.getVal(uid);
        if (!cacheKit.setVal(uid, token, 0))
            return false;
        if (!V.isEmpty(oldToken))
            cacheKit.deleteVal(oldToken);

        return cacheKit.setVal(token, J.o2s(user), SESSION_TIME);
    }


    /**
     * 更新缓存
     */
    public Boolean updateCacheUser(String token, JSONObject user) {
        return cacheKit.setVal(token, J.o2s(user), 0);
    }


    public String getNumberByCache(String token) {
//        JSONObject object = getTeacherInfo(token);
        String json = cacheKit.getVal(token);
        JSONObject object = JSON.parseObject(json);
        if (object.getString("number") != null) {
            return object.getString("number");
        } else {
            return null;
        }
    }

    public String getUserIdByCache(String token) {
//        JSONObject object = getTeacherInfo(token);
        String json = cacheKit.getVal(token);
        JSONObject object = J.s2j(json);
        if (object.getString("id") != null) {
            return object.getString("id");
        } else {
            return null;
        }
    }


    public String getAcademyCodeByCache(String token) {
        String json = cacheKit.getVal(token);
        JSONObject object = J.s2j(json);
        if (object.getString("academyCode") != null) {
            return object.getString("academyCode");
        } else {
            return null;
        }
    }

    public Integer getVisibleByCache(String token) {
        String json = cacheKit.getVal(token);
        JSONObject object = J.s2j(json);
        if (object.getInteger("visible") != null) {
            return object.getInteger("visible");
        } else {
            return null;
        }
    }

    public String getLoginNumberCache(String token) {
        String json = cacheKit.getVal(token);
        JSONObject object = J.s2j(json);
        if (object.getString("loginNumber") != null) {
            return object.getString("loginNumber");
        } else {
            return null;
        }
    }


    public String getSuffix(String _fileName) {
        String suffix = "";
        String[] item = _fileName.split("\\.");
        if (item.length > 0) {
            suffix = item[(item.length - 1)];
        }
        suffix.toLowerCase();
        return suffix;
    }


    public String generateString(int length) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

}
