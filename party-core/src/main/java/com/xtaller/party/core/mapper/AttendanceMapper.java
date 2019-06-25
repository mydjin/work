package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Attendance;

import java.util.List;
/**
 * Created by party on 2018/08/13
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {


    // FIXME:批量插入
    @Insert("INSERT INTO attendance (id,number,scheduleId,type,creator,createTime)VALUES " +
            "${values} ; ")
    int inserRows(@Param("values") String values);


    @Select("SELECT u.name,a.*, " +
            "FROM_UNIXTIME(attendanceTime) attendanceTimeStr ,c.topic " +
            " FROM attendance a " +
            "JOIN (SELECT id from attendance where isDel = 0 ${where} " +
            ")b ON a.id=b.id " +
            "JOIN `schedule` c on c.id=a.scheduleId AND c.status != 1 " +
            "LEFT JOIN user_base_info u ON u.number = a.number " +
            "order by a.createTime desc LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("SELECT count(1) total  FROM attendance a " +
            "JOIN (SELECT id from attendance where isDel = 0 ${where} " +
            ")b ON a.id=b.id " +
            "JOIN `schedule` c on c.id=a.scheduleId AND c.status != 1 " +
            "LEFT JOIN user_base_info u ON u.number = a.number ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM attendance a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);



}