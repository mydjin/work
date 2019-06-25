package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreSemester;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/12/22
*/
public interface CoreSemesterMapper extends BaseMapper<CoreSemester> {
    @Select("SELECT a.* FROM core_semester a " +
            "JOIN (SELECT id from core_semester where  1=1 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.id asc ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from core_semester where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("select * from core_semester where id=#{id} ")
    JSONObject getById(@Param("id") String id);
}