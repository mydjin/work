package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Note;

import java.util.List;
/**
 * Created by party on 2018/08/17
 */
public interface NoteMapper extends BaseMapper<Note> {

    @Select("SELECT a.* , " +
            "FROM_UNIXTIME(sendTime) sendTimeStr  " +
            " FROM note a " +
            "JOIN (SELECT id from note where isDel = 0 ${where} order by SendTime desc " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.SendTime desc,a.type,a.theme ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from note where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM note a where 1=1 and isDel=0 ${where} order by SendTime desc,type,theme")
    List<JSONObject> queryAll(@Param("where") String where);
}