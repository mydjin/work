package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.IntroduceParty;

import java.util.List;
/**
 * Created by party on 2018/10/08
 */
public interface IntroducePartyMapper extends BaseMapper<IntroduceParty> {

    @Select("SELECT a.* " + 
            ",FROM_UNIXTIME(introduceJoinTime) introduceJoinTimeStr  " +
              "FROM introduce_party a " + 
            "JOIN (SELECT id from introduce_party where isDel = 0 ${where} " + 
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from introduce_party where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM introduce_party a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}