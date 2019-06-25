package com.qihsoft.webdev.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* Created by Qihsoft on 2017/11/08
*/
public interface CustomMapper extends BaseMapper<JSONObject> {



    @Select("SELECT * from v_message_user WHERE number= #{number}  ")
    JSONObject  getMssageUserByNumber(@Param("number") String number);

    @Select("SELECT * from v_wechat_message_user WHERE number= #{number}  ")
    JSONObject  getWechatMessageUserByNumber(@Param("number") String number);

    @Select("SELECT token openId,id from sys_user_token WHERE isDel=0  and  userId =#{userId}  and type=#{type}")
    JSONObject  getTokenByIdAndType(@Param("userId") String userId,@Param("type") String type);

    @Select("SELECT a.userId,a.id FROM sys_user_token a where isDel=0  and type= '${type}' and token= '${token}' ")
    JSONObject queryUserIdByToken(@Param("type") String type,@Param("token") String token);



}