package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.model.Section;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.OnlineCourse;

import java.util.List;
/**
 * Created by party on 2018/08/23
 */
public interface OnlineCourseMapper extends BaseMapper<OnlineCourse> {
    @Select("SELECT " +
            "a.*, " +
            "FROM_UNIXTIME( startTime ) AS startTimeStr, " +
            "FROM_UNIXTIME( endTime ) AS endTimeStr, " +
            "b.artworkURL,b.signURL " +
            "FROM " +
            "online_course a " +
            "LEFT JOIN v_sign_picture b ON a.pictureId = b.id " +
            "WHERE " +
            "a.isDel = 0 ${where}" +
            "ORDER BY " +
            "a.createTime ASC " +
            "LIMIT #{index},#{size} " )
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from online_course a where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select(" SELECT  " +
            " a.id, a.topic, a.title, pictureId, c.artworkURL,c.signURL " +
            " FROM  " +
            " online_course a   " +
            " JOIN (SELECT id FROM online_course WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            " LEFT JOIN v_sign_picture c on c.id = a.pictureId " +
            " ORDER BY " +
            " a.createTime DESC " +
            " LIMIT #{index},#{size} " )
    List<JSONObject> pcGetPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select(" SELECT  " +
            " a.id, a.topic, a.title, pictureId, c.artworkURL ,c.signURL,d.learnPercent,d.score,d.isFinish " +
            " FROM  " +
            " online_course a   " +
            " JOIN (SELECT id FROM online_course WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            " LEFT JOIN v_sign_picture c on c.id = a.pictureId " +
            " LEFT JOIN online_course_user d on d.courseId=a.id and d.number = ${number} "+
            " ORDER BY " +
            " a.createTime DESC " +
            " LIMIT #{index},#{size} " )
    List<JSONObject> pcGetPage_note(@Param("where") String where,
                                    @Param("index") int index,
                                    @Param("size") int size,
                                    @Param("number") String number);



    @Select("select count(1) total FROM  " +
            "online_course a   " +
            "JOIN (SELECT id FROM online_course WHERE isDel = 0 ${where}) b on a.id = b.id ")
    JSONObject pcGetPageCount(@Param("where") String where);


    @Select("SELECT * from" +
            "(SELECT c.id, c.topic, c.title, pictureId, artworkURL ,signURL,a.number,a.learnPercent,a.score,a.isFinish,c.isRelease,c.isDel,c.createTime" +
            "    FROM  online_course_user  a   JOIN (SELECT id,courseId FROM online_course_user WHERE isDel = 0 and number = '${number}')b on a.id =b.id " +
            "left join online_course c on c.id =b.courseId   left join v_sign_picture d on d.id =c.pictureId)e where isDel =0 ${where} "+
            " ORDER BY " +
            " createTime DESC " +
            " LIMIT #{index},#{size} " )
    List<JSONObject> pcGetPageByRange(@Param("where") String where,
                                      @Param("index") int index,
                                      @Param("size") int size,
                                      @Param("number") String number);

    @Select("SELECT count(1) total from" +
            "(SELECT c.id, c.topic, c.title, pictureId, artworkURL ,signURL,a.number,a.learnPercent,a.score,a.isFinish,c.isRelease,c.isDel,c.createTime" +
            "    FROM  online_course_user  a   JOIN (SELECT id,courseId FROM online_course_user WHERE isDel = 0 and number = '${number}')b on a.id =b.id " +
            "left join online_course c on c.id =b.courseId   left join v_sign_picture d on d.id =c.pictureId)e where isDel =0 ${where} "
            )
    JSONObject pcGetPageByRangeCount(@Param("where") String where,
                              @Param("number") String number);

    @Select("SELECT  " +
            " a.*, c.artworkURL,signURL " +
            "FROM  " +
            "online_course a   " +
            "JOIN (SELECT id FROM online_course WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            "LEFT JOIN v_sign_picture c on c.id = a.pictureId "+
            " order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT  " +
            " a.*, c.artworkURL,d.learnPercent,d.score,d.isFinish,signURL " +
            "FROM  " +
            "online_course a   " +
            "JOIN (SELECT id FROM online_course WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            "LEFT JOIN v_sign_picture c on c.id = a.pictureId "+
            " LEFT JOIN online_course_user d on d.courseId=a.id and d.number = ${number} "+
            " order by createTime desc")
    List<JSONObject> queryAll_user(@Param("where") String where,@Param("number") String number);

    @Select("SELECT  CONCAT('[',a.topic,']',a.title) FROM online_course a where 1=1 and isDel=0 and id=${id} ")
    String queryNameById(@Param("id") String id);

    @Select("SELECT  a.*, c.artworkURL,signURL,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr FROM online_course a  "+
           "JOIN (SELECT id FROM online_course WHERE isDel = 0 and id= ${id} ) b on a.id = b.id   " +
            "LEFT JOIN v_sign_picture c on c.id = a.pictureId ")
    JSONObject queryById(@Param("id") String id);

    @Select("SELECT *,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr FROM online_course where 1=1 and isDel=0 ${where}  ")
    List<JSONObject>  query(@Param("where") String where);


    @Select(" SELECT  " +
            " a.id, a.title, pictureId, c.artworkURL,signURL,d.learnPercent " +
            " FROM  " +
            " online_course a   " +
            " JOIN (SELECT id FROM online_course WHERE isDel = 0 ) b on a.id = b.id    " +
            " JOIN v_sign_picture c on c.id = a.pictureId " +
            " JOIN online_course_user d on d.courseId=a.id and d.number = ${number} "+
            " ORDER BY " +
            " a.createTime DESC " +
            " LIMIT #{index},#{size} " )
    List<JSONObject> wechatGetPage_CourseLearn(
                                   @Param("index") int index,
                                   @Param("size") int size,
                                   @Param("number") String number
                                   );

    @Select(
            "SELECT  c.id,c.title,signURL,p.artworkURL " +
            "FROM online_course c " +
            "JOIN (SELECT id FROM online_course WHERE isDel = 0 ) b on c.id = b.id " +
            "JOIN v_sign_picture p on p.id = c.pictureId " +
            "JOIN online_course_user u on u.courseId=c.id and u.number = ${number} " +
            "order by u.createTime desc "
    )
    List<JSONObject> queryAll_CourseName(@Param("number") String number);



}