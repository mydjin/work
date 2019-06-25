package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreTimetable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/01/24
*/
public interface CoreTimetableMapper extends BaseMapper<CoreTimetable> {
    @Select("SELECT a.* FROM v_timetable a " +
            "JOIN (SELECT id from v_timetable where  1=1  ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.semesterId desc ,a.classId asc ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from v_timetable where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);
}