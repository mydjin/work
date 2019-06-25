package com.qihsoft.webdev.core.tps;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.utils.convert.J;
import com.qihsoft.webdev.utils.convert.V;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Qihsoft
 * @create 2017-12-01 9:56
 */
@Component
public class CacheKit {
    @Value("${swagger2.enable}")
    private boolean swagger2Enable;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    // 获取所有登陆用户
    public List<JSONObject> getAllLoginUsers(){
        if(swagger2Enable) {
            List<JSONObject> list = new ArrayList<>();

            Set<String> keys = stringRedisTemplate.keys("*");
            for (String key : keys) {
                if (key.length() == 32) {
                    String obj = stringRedisTemplate.opsForValue().get(key);
                    if(obj.indexOf("term")!=-1) {
                        JSONObject userObj = JSONObject.parseObject(obj);

                        list.add(userObj);
                    }

                }
            }

         return list ;

        }else{
            return  null;
        }
    }

    // 获取所有登陆令牌
    public List<JSONObject> getAllLoginToken(){
        if(swagger2Enable) {

            List<JSONObject> list = new ArrayList<>();

        Set<String> keys = stringRedisTemplate.keys("*");
        for(String key : keys){
            if(key.length()==32) {
                String obj = stringRedisTemplate.opsForValue().get(key);
                if(obj.indexOf("term")!=-1) {
                    JSONObject infoObj =new JSONObject();

                    infoObj .put("token",key);
                    list.add(infoObj);
                }
            }
        }

        return list ;
        }else{
            return  null;
        }
    }



    // 字符串设置
    public Boolean setVal(String key, String val, int times){
        try {
            if (times > 0) {
                stringRedisTemplate.opsForValue().set(key, val, times, TimeUnit.SECONDS);
            }else {
                times = 604800;
                stringRedisTemplate.opsForValue().set(key, val, times, TimeUnit.SECONDS);
                // stringRedisTemplate.opsForValue().set(key, val);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // 字符串读取
    public String getVal(String key){
        String val = "";
        try{
            val = stringRedisTemplate.opsForValue().get(key);
            val = val == null ? "" : val;
        }catch (Exception ex){}
        return val;
    }

    // 字符串读取
    public String getWechatVal(String key){
        if(!swagger2Enable) {
            String val = "";
            try {
                val = stringRedisTemplate.opsForValue().get(key);
                val = val == null ? "" : val;
            } catch (Exception ex) {
            }
            return val;
        }else{
            return  null;
        }
    }

    public JSONObject user(String key){
        if(V.isEmpty(getVal(key))){
            return  null;
        }else {
            return J.s2j(getVal(key));
        }
    }
    public String getUserId(String key){
        if(V.isEmpty(user(key))){
            return  null;
        }else{
            return user(key).getString("id");
        }

    }

    public String getNumberByCache(String token) {
//        JSONObject object = getTeacherInfo(token);
        String json = getVal(token);
        JSONObject object = J.s2j(json);
        if (object != null) {
            return object.getString("number");
        } else {
            return null;
        }
    }

    // 字符串删除
    public void deleteVal(String key){
        stringRedisTemplate.delete(key);
    }

    // 对象设置
    public void setObject(String key, Object object, int times){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if (times > 0) {
            operations.set(key, object, times, TimeUnit.SECONDS);
        }else {
            times = 604800;
            operations.set(key, object, times, TimeUnit.SECONDS);
            //operations.set(key, object);
        }
    }

    // 对象读取
    public JSONObject getObject(String key){
        JSONObject object = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        try{
            object = J.o2j(operations.get(key));
        }catch (Exception e){}
        return object;
    }

    // 删除对象
    public void deleteObject(String key){
        redisTemplate.delete(key);
    }



}
