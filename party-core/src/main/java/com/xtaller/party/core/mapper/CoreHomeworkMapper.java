package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreHomework;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/01/03
*/
public interface CoreHomeworkMapper extends BaseMapper<CoreHomework> {
    @Select("SELECT a.* FROM v_homework a " +
            "JOIN (SELECT id from v_homework where isDel=0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime desc")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from v_homework where isDel=0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("select * from v_homework where isDel=0 ${where} ")
    JSONObject findViewById(@Param("where") String where);
}