package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Vacate;

import java.util.List;

/**
 * Created by party on 2018/08/15
 */
public interface VacateMapper extends BaseMapper<Vacate> {


    @Select("SELECT a.*, b.topic,u.name,FROM_UNIXTIME(a.createTime) createTimeStr  FROM vacate a JOIN schedule b " +
            "ON 1=1 and a.isDel=0 and b.isDel=0 and a.scheduleId=b.id ${where} " +
            "JOIN user_base_info u ON a.number = u.number AND u.isDel = 0 " +
            "order by a.createTime desc LIMIT #{index}, #{size}")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("SELECT count(1) total FROM vacate a JOIN schedule b " +
            "ON 1=1 and a.isDel=0 and b.isDel=0 and a.scheduleId=b.id ${where} " +
            "JOIN user_base_info u ON a.number = u.number AND u.isDel = 0 "  )
    JSONObject getPageCount(@Param("where") String where);


    @Select("SELECT a.*, b.topic,FROM_UNIXTIME(a.createTime) createTimeStr  FROM vacate a,schedule b where 1=1 and a.isDel=0 and b.isDel=0 and a.scheduleId=b.id ${where}  order by a.createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);


    @Select("SELECT a.*, b.topic,u.name,FROM_UNIXTIME(a.createTime) createTimeStr  FROM vacate a " +
            "JOIN (select id from vacate where id = ${id} )c on c.id =a.id " +
            "left JOIN schedule b ON 1=1 and a.isDel=0 and b.isDel=0 and a.scheduleId=b.id  " +
            "left JOIN user_base_info u ON a.number = u.number AND u.isDel = 0 ")
    JSONObject getById(@Param("id") String id);
}