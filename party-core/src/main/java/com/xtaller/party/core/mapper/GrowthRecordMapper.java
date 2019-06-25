package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.GrowthRecord;

import java.util.List;
/**
 * Created by party on 2018/08/20
 */
public interface GrowthRecordMapper extends BaseMapper<GrowthRecord> {

    @Select("SELECT a.*,u.name,FROM_UNIXTIME(recordTime,'%Y-%m-%d') recordTimeStr " +
            "FROM growth_record a " +
            "JOIN (SELECT id from growth_record where isDel = 0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id " +
            "LEFT JOIN user_base_info u on u.number =  a.number " +
            "order by a.createTime asc ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from growth_record where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.*,u.name FROM growth_record a JOIN (SELECT id from growth_record where isDel = 0 ${where})  b ON a.id=b.id " +
            "LEFT JOIN user_base_info u on u.number =  a.number order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}