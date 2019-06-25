package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Schedule;

import java.util.List;

/**
 * Created by party on 2018/08/13
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {
    @Select("SELECT a.*, " +
            "FROM_UNIXTIME(startTime) startTimeStr, " +
            "FROM_UNIXTIME(endTime) endTimeStr " +
            "FROM schedule a " +
            "JOIN (SELECT id from schedule where isDel = 0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ")
    List<JSONObject> adminGetPage(@Param("where") String where,
                                  @Param("index") int index,
                                  @Param("size") int size);

    @Select(" SELECT " +
            " a.id, a.topic, a.startTime, a.place, a.speaker, a.status, c.status AS attendanceStatus,, FROM_UNIXTIME( a.startTime ) startTimeStr,FROM_UNIXTIME( a.endTime ) endTimeStr," +
            "a.position schedulePosition,a.remark scheduleRemark,c.position attendancePosition,c.remark attendanceRemark " +
            " FROM " +
            " `schedule` a " +
            " JOIN (SELECT id FROM `schedule` WHERE isDel = 0 ${where}) b on a.id = b.id  " +
            " LEFT JOIN attendance c on c.scheduleId = a.id" +
            " ORDER BY " +
            " a.createTime DESC " +
            " LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from schedule where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);


    //获取会议列表 htc (lw 0522修改)
    @Select(" SELECT s.id,s.type, s.topic, FROM_UNIXTIME( s.startTime ) startTimeStr,FROM_UNIXTIME( s.endTime ) endTimeStr, s.place, s.summary," +
            " s.speaker, s.`status` AS schedulleStatus, s.isDel, s.createTime , a.status attendanceStatus," +
            "s.position schedulePosition,s.remark scheduleRemark,a.position attendancePosition,a.remark attendanceRemark  " +
            " FROM `schedule` s " +
            "  JOIN attendance a ON a.scheduleId = s.id AND s.isDel =0 AND a.isDel =0 " +
            "  AND s.status !=1  AND  s.type = #{type} AND a.number = ${number} " +
            " ORDER BY s.startTime LIMIT #{index},#{size} ")
    List<JSONObject> pcGetUserPage(@Param("where") String where,
                                    @Param("number") String number,
                                    @Param("type") int type,
                                    @Param("index") int index,
                                    @Param("size") int size);

    @Select(" SELECT count(1) total " +
            " FROM `schedule` s " +
            "  JOIN attendance a ON a.scheduleId = s.id AND s.isDel =0 AND a.isDel =0  " +
            "  AND s.status !=1  AND  s.type = #{type} AND a.number = ${number} " )
    JSONObject pcGetUserPageCount(@Param("where") String where,
                                   @Param("number") String number,
                                   @Param("type") int type);

    @Select("SELECT a.*, " +
            "FROM_UNIXTIME(startTime) startTimeStr, " +
            "FROM_UNIXTIME(endTime) endTimeStr " +
            "FROM schedule a where 1=1 and isDel=0  ${where}  order by a.status,createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);


    //获取全部详情 htc (lw 0522修改)
    @Select("SELECT v.id vacateId,v.reason,v.type,v.number,v.result,v.`status` vacateStatus,v.opinion,v.approverNumber, " +
            "a.status attendanceStatus,a.position attendancePosition,a.remark attendanceRemark, "+
            "s.id, s.topic,s.place, s.speaker, s.summary,s.status, " +
            "FROM_UNIXTIME( s.startTime ) startTimeStr," +
            "FROM_UNIXTIME( s.endTime ) endTimeStr " +
            "FROM `schedule` s " +
            "JOIN attendance a ON a.scheduleId = s.id AND s.isDel = 0 AND a.isDel = 0 " +
            "AND  s.id = ${id} AND a.number = ${number} AND s.status !=1   " +
            "LEFT JOIN vacate v ON v.isDel = 0 AND v.number = a.number AND s.id = v.scheduleId")
    List<JSONObject> getAllDetail(@Param("id") String id,
                                     @Param("number") String number);

    //获取请假详情 htc (lw 0522修改)
    @Select("SELECT v.id vacateId,v.reason,v.type,v.number,v.result,v.`status` vacateStatus,v.opinion,v.approverNumber," +
            "s.id, s.topic," +
            "FROM_UNIXTIME( s.startTime ) startTimeStr," +
            "FROM_UNIXTIME( s.endTime ) endTimeStr," +
            "s.place, s.speaker, s.summary,s.status " +
            "FROM `schedule` s " +
            "JOIN attendance a ON a.scheduleId = s.id AND s.isDel = 0 AND a.isDel = 0 " +
            "AND  s.id = ${id} AND a.number = ${number} AND s.status !=1   " +
            "LEFT JOIN vacate v ON v.isDel = 0 AND v.number = a.number AND s.id = v.scheduleId")
    List<JSONObject> getVacateDetail(@Param("id") String id,
                               @Param("number") String number);


    //获取签到详情  lw (lw 0522修改)
    @Select(" SELECT s.id,s.type, s.topic, FROM_UNIXTIME( s.startTime ) startTimeStr,FROM_UNIXTIME( s.endTime ) endTimeStr, s.place, s.summary," +
            " s.speaker, s.`status` AS schedulleStatus, s.isDel, s.createTime , a.status attendanceStatus," +
            "a.position attendancePosition,a.remark attendanceRemark " +
            " FROM `schedule` s " +
            "JOIN attendance a ON a.scheduleId = s.id AND s.isDel = 0 AND a.isDel = 0 " +
            "AND  s.id = ${id} AND a.number = ${number} AND s.status !=1   "  )
    List<JSONObject> getAttendanceDetail(@Param("id") String id,
                                   @Param("number") String number);


}