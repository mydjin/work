package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.ExamRoom;
import org.apache.ibatis.annotations.Update;

import java.util.List;
/**
 * Created by Party on 2019/05/28
 */
public interface ExamRoomMapper extends BaseMapper<ExamRoom> {

    @Select("SELECT a.*,e.name examName,e.status examStatus,releaseStatus,startTime,endTime,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr " +
              "FROM exam_room a " + 
            "JOIN (SELECT id from exam_room where isDel = 0 ${where} " +
            ")b ON a.id=b.id join (select id,name,status,releaseStatus,startTime,endTime  from exam where isDel=0) e on e.id=a.examId order by a.createTime desc LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from exam_room where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.*,e.name examName,e.status examStatus,releaseStatus,startTime,endTime,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr  FROM exam_room a join (select id,name,status,releaseStatus,startTime,endTime  from exam where isDel=0) e on e.id=a.examId  where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.*,e.name examName,e.status examStatus,releaseStatus,startTime,endTime,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr " +
            "FROM exam_room a " +
            "JOIN (SELECT id from exam_room where isDel = 0 and id = ${id}" +
            ")b ON a.id=b.id join (select id,name,status,releaseStatus,startTime,endTime  from exam where isDel=0) e on e.id=a.examId ")
    JSONObject queryInfo(@Param("id") String id);

    @Update("update exam_candidate  set roomId = null where 1=1 and isDel=0 and roomId =  ${roomId} ")
    Integer deleteStudentByRoomId(@Param("roomId") String roomId);

    @Select("SELECT number  FROM exam_candidate   where 1=1 and isDel=0 and roomId =  ${roomId}  ")
    String[] queryStudentsByRoomId(@Param("roomId") String roomId);
}