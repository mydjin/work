package com.qihsoft.webdev.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysOperationRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Qihsoft on 2018/05/08
*/
public interface SysOperationRecordMapper extends BaseMapper<SysOperationRecord> {



    @Select("SELECT a.*,FROM_UNIXTIME(createTime) createTimeStr FROM v_operation_record a where 1=1 ${where} order by createTime desc LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from  v_operation_record where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);


    @Select("SELECT a.*,FROM_UNIXTIME(createTime) createTimeStr FROM v_operation_record a where 1=1 ${where} order by createTime desc ")
    List<JSONObject> queryAll(@Param("where") String where);

    //微信端获取最近五条登陆日志
    @Select("SELECT function,ipAddr,status,createTime,createTimeStr  FROM v_operation_record  where 1=1  and number = ${number} and control = '系统授权管理'and server ='1' order by createTime desc LIMIT 3 ")
    List<JSONObject> queryWechatLastLoginLogByNumber(@Param("number") String number);

}