package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.ScheduleStudent;

import java.util.List;
/**
 * Created by party on 2018/08/14
 */
public interface ScheduleStudentMapper extends BaseMapper<ScheduleStudent> {
    // fixme:批量插入
    @Insert("INSERT INTO schedule_student (id,number,scheduleId,type,creator,createTime)VALUES " +
            "${values} ; ")
    int inserRows(@Param("values") String values);

    @Select("SELECT a.* FROM schedule_student a " +
            "JOIN (SELECT id from schedule_student where isDel = 0 ${where} " + 
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from schedule_student where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM schedule_student a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.* FROM v_schedule_student a where 1=1 ${where}  order by number desc")
    List<JSONObject> queryInfo(@Param("where") String where);
}