package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.ExamCandidate;

import java.util.List;
/**
 * Created by party on 2018/09/01
 */
public interface ExamCandidateMapper extends BaseMapper<ExamCandidate> {


    // fixme:批量插入
    @Insert("INSERT INTO exam_candidate (id,number,examId,creator,createTime)VALUES " +
            "${values} ; ")
    int insertRows(@Param("values") String values);

//reviseTime
    @Select("SELECT *,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr,FROM_UNIXTIME(ansTime) ansTimeStr  from v_exam_candidate where 1=1  ${where}  order by releaseStatus asc, startTime desc  LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from v_exam_candidate where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT *,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr,FROM_UNIXTIME(ansTime) ansTimeStr from v_exam_candidate where 1=1  ${where}  order by releaseStatus asc, startTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT number  FROM exam_candidate   where 1=1 and isDel=0 and examId =  ${examId} ")
    String[] queryStudentsByExamId(@Param("examId") String examId );


    @Select("SELECT a.id,a.examId,a.status,a.examStatus,a.name examName,u.number,u.name,academyName,a.score,a.remark,a.ipAddr,myClass,FROM_UNIXTIME(a.ansTime) ansTimeStr   " +
            "FROM v_exam_candidate a join (select id from v_exam_candidate where 1=1 ${exam_where} )b on a.id = b.id  " +
            " join (select number,name,academyName,academyCode,myClass from v_user_info where isDel=0 ${user_where} ) u on  a.number=u.number where 1=1  order by releaseStatus asc,startTime desc,examId,a.number   LIMIT #{index}, #{size}")
    List<JSONObject> getPageGrade_userSelect(@Param("exam_where") String exam_where,
                                             @Param("user_where") String user_where,
                                             @Param("index") int index,
                                             @Param("size") int size);


    @Select("select count(1) total FROM v_exam_candidate a " +
            "join ( select id from   v_exam_candidate where 1=1 ${exam_where} )b on a.id=b.id " +
            " join (" +
            "SELECT `u`.`name` AS `name`,`u`.`number` AS `number`,`udi`.`academyCode` AS `academyCode`,`udi`.`myClass` AS `myClass`,`ai`.`name` AS `academyName`   " +
            "FROM`user_base_info` `u`   " +
            "JOIN ( SELECT id FROM `user_base_info` WHERE  isDel = 0   ) ubi ON  u.id =  ubi.id   " +
            "JOIN (SELECT number,academyCode,myClass FROM   `user_detail_info`   WHERE   isDel = 0  ) udi ON  u.number =  udi.number   " +
            "JOIN (SELECT code,name from academy_info where isDel=0) ai ON  `udi`.`academyCode` =  `ai`.`code`   " +
            "where isDel = 0   ${ubi_where})u on  a.number=u.number where 1=1 order by releaseStatus asc,startTime desc,examId,u.number " )
    JSONObject getPageGradeCount_userSelect(@Param("exam_where") String exam_where,@Param("ubi_where") String ubi_where);

    @Select("SELECT   a.id,  a.STATUS, a.examid,a.examStatus, a.NAME examName,   roomName,  u.number, u.NAME,  u.academyName,  a.score,  a.remark,   a.ipAddr,  myClass,  FROM_UNIXTIME( a.ansTime ) ansTimeStr  FROM   v_exam_candidate a  " +
            " join (SELECT id from v_exam_candidate where 1 = 1   ${exam_where}   order by releaseStatus asc,startTime desc,examId,number LIMIT #{index}, #{size}  )b on  a .id =b.id  " +
            " join (" +
            "SELECT `u`.`name` AS `name`,`u`.`number` AS `number`,`udi`.`academyCode` AS `academyCode`,`udi`.`myClass` AS `myClass`,`ai`.`name` AS `academyName`   " +
            "FROM`user_base_info` `u`   " +
            "JOIN ( SELECT id FROM `user_base_info` WHERE  isDel = 0  ) ubi ON  u.id =  ubi.id   " +
            "JOIN (SELECT number,academyCode,myClass FROM   `user_detail_info`    WHERE   isDel = 0 ) udi ON  u.number =  udi.number   " +
            "JOIN (SELECT code,name from academy_info where isDel=0) ai ON  `udi`.`academyCode` =  `ai`.`code`   " +
            "where isDel = 0 )u " +
            "on a.number=u.number " +
            " where 1=1 order by releaseStatus asc,startTime desc,examId,u.number  ")
    List<JSONObject> getPageGrade_examSelect(@Param("exam_where") String exam_where,
                                  @Param("index") int index,
                                  @Param("size") int size);


    @Select("select count(1) total FROM v_exam_candidate a " +
            "join ( select id from   v_exam_candidate where 1=1 ${exam_where} )b on a.id=b.id  where 1=1  ")
    JSONObject getPageGradeCount_examSelect(@Param("exam_where") String exam_where);


    @Select("SELECT   a.id,  a.STATUS, a.examid,a.examStatus, a.NAME examName,   roomName,  u.number, u.NAME,  u.academyName,  a.score,  a.remark,   a.ipAddr,  myClass,  FROM_UNIXTIME( a.ansTime ) ansTimeStr  FROM   v_exam_candidate a  " +
            " join (SELECT id from v_exam_candidate where 1 = 1   ${exam_where}  order by releaseStatus asc,startTime desc,number   )b on  b .id =a.id  " +
            " join (" +
            "SELECT `u`.`name` AS `name`,`u`.`number` AS `number`,`udi`.`academyCode` AS `academyCode`,`udi`.`myClass` AS `myClass`,`ai`.`name` AS `academyName`   " +
            "FROM`user_base_info` `u`   " +
            "JOIN ( SELECT id FROM `user_base_info` WHERE  isDel = 0  ) ubi ON  u.id =  ubi.id   " +
            "JOIN (SELECT number,academyCode,myClass FROM   `user_detail_info`    WHERE   isDel = 0 ) udi ON  u.number =  udi.number   " +
            "JOIN (SELECT code,name from academy_info where isDel=0) ai ON  `udi`.`academyCode` =  `ai`.`code`   " +
            "where isDel = 0 )u " +
            "on a.number=u.number " +
            " where 1=1 order by releaseStatus asc,startTime desc,u.number   ")
    List<JSONObject> queryAllGrade(@Param("exam_where") String exam_where);

    @Select("SELECT   a.id,  a.STATUS, a.examid,a.examStatus, a.NAME examName,   roomName,  u.number, u.NAME,  u.academyName,  a.score,  a.remark,   a.ipAddr,  myClass,  FROM_UNIXTIME( a.ansTime ) ansTimeStr  FROM   v_exam_candidate a  " +
            " join (SELECT id from v_exam_candidate where 1 = 1   ${exam_where}  order by  releaseStatus asc, startTime desc,number   )b on  b .id =a.id  " +
            " join (" +
            "SELECT `u`.`name` AS `name`,`u`.`number` AS `number`,`udi`.`academyCode` AS `academyCode`,`udi`.`myClass` AS `myClass`,`ai`.`name` AS `academyName`   " +
            "FROM`user_base_info` `u`   " +
            "JOIN ( SELECT id FROM `user_base_info` WHERE  isDel = 0  ) ubi ON  u.id =  ubi.id   " +
            "JOIN (SELECT number,academyCode,myClass FROM   `user_detail_info`    WHERE   isDel = 0 ) udi ON  u.number =  udi.number   " +
            "JOIN (SELECT code,name from academy_info where isDel=0) ai ON  `udi`.`academyCode` =  `ai`.`code`   " +
            "where isDel = 0  ${ubi_where})u " +
            "on a.number=u.number " +
            " where 1=1 order by releaseStatus asc,startTime desc,u.number   ")
    List<JSONObject> queryAllGrade_user(@Param("exam_where") String exam_where,@Param("ubi_where") String ubi_where);


    @Select("SELECT rank FROM(  " +
            "            SELECT b.*,   " +
            "            CASE WHEN @rowtotal = score THEN   @rownum   " +
            "            WHEN @rowtotal := score THEN   @rownum :=@rownum + 1   " +
            "            WHEN @rowtotal = 0 THEN   @rownum :=@rownum + 1   " +
            "            END AS rank    " +
            "            FROM (SELECT a.number,a.score from v_exam_candidate a where 1 = 1  and examId = ${examId}  order by score desc " +
            ")b,(SELECT @rownum := 0 ,@rowtotal := NULL)r   " +
            "            )c  WHERE number= ${number}  ")
   int  getRank(@Param("examId") String examId,@Param("number") String number);

    @Select("SELECT a.*,FROM_UNIXTIME(startTime) startTimeStr,FROM_UNIXTIME(endTime) endTimeStr,FROM_UNIXTIME(ansTime) ansTimeStr from v_exam_candidate a where 1 = 1  ${where}  order by releaseStatus asc, startTime desc  ")
    List<JSONObject>  queryPersonExamAll(@Param("where") String where);



     //年榜
    @Select("SELECT * FROM(  " +
            "SELECT a.*,  " +
            "CASE WHEN @rowtotal = avgScore THEN   @rownum  " +
            "WHEN @rowtotal := avgScore THEN   @rownum :=@rownum + 1  " +
            "WHEN @rowtotal = 0 THEN   @rownum :=@rownum + 1  " +
            "END AS rank  " +
            "FROM ( SELECT * FROM v_ranking_year  where 1=1 ${where}  ORDER BY avgScore desc,subyear desc)a,(SELECT @rownum := 0 ,@rowtotal := NULL)r  " +
            ")b      LIMIT #{index}, #{size} ")
    List<JSONObject> getRankYearPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select("select count(1) total from v_ranking_year where 1=1 ${where} ")
    JSONObject getRankYearPageCount(@Param("where") String where);

    //个人年榜
    @Select("SELECT * FROM(  " +
            "SELECT a.*,  " +
            "CASE WHEN @rowtotal = avgScore THEN   @rownum  " +
            "WHEN @rowtotal := avgScore THEN   @rownum :=@rownum + 1  " +
            "WHEN @rowtotal = 0 THEN   @rownum :=@rownum + 1  " +
            "END AS rank  " +
            "FROM ( SELECT * FROM v_ranking_year  where 1=1 ${where}  ORDER BY avgScore desc,subyear desc)a,(SELECT @rownum := 0 ,@rowtotal := NULL)r  " +
            ")b      where number= ${number} ")
    JSONObject getRankYearUser(@Param("where") String where,
                                     @Param("number") String number);



    //月榜
    @Select("SELECT * FROM(  " +
            "SELECT a.*,  " +
            "CASE WHEN @rowtotal = avgScore THEN   @rownum  " +
            "WHEN @rowtotal := avgScore THEN   @rownum :=@rownum + 1  " +
            "WHEN @rowtotal = 0 THEN   @rownum :=@rownum + 1  " +
            "END AS rank  " +
            "FROM ( SELECT * FROM v_ranking_month   where 1=1 ${where}  ORDER BY avgScore desc,subyear desc,submonth desc  )a,(SELECT @rownum := 0 ,@rowtotal := NULL)r  " +
            ")b     LIMIT #{index}, #{size} ")
    List<JSONObject> getRankMonthPage(@Param("where") String where,
                                    @Param("index") int index,
                                    @Param("size") int size);


    @Select("select count(1) total from v_ranking_month where 1=1 ${where} ")
    JSONObject getRankMonthPageCount(@Param("where") String where);


    //月榜
    @Select("SELECT * FROM(  " +
            "SELECT a.*,  " +
            "CASE WHEN @rowtotal = avgScore THEN   @rownum  " +
            "WHEN @rowtotal := avgScore THEN   @rownum :=@rownum + 1  " +
            "WHEN @rowtotal = 0 THEN   @rownum :=@rownum + 1  " +
            "END AS rank  " +
            "FROM ( SELECT * FROM v_ranking_month   where 1=1 ${where}  ORDER BY avgScore desc,subyear desc,submonth desc  )a,(SELECT @rownum := 0 ,@rowtotal := NULL)r  " +
            ")b    where number= ${number}  ")
    JSONObject getRankMonthUser(@Param("where") String where,
                                @Param("number") String number);




}