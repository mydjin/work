package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Report;

import java.util.List;

/**
 * Created by party on 2018/08/10
 */
public interface ReportMapper extends BaseMapper<Report> {

    //    @Select("SELECT a.* , " +
//            "FROM_UNIXTIME(referTime,'%Y-%m-%d') referTimeStr  " +
//            "FROM report a " +
//            "JOIN (SELECT id from report where isDel = 0 ${where} " +
//            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ")
//    List<JSONObject> getPage(@Param("where") String where,
//                             @Param("index") int index,
//                             @Param("size") int size);
    @Select("SELECT  u.name userName,a.*," +
            "b.stage approvalStage,b.status,b.result,b.opinion,approverNumber,approvalType,b.id As approvalTableId, " +
            "FROM_UNIXTIME( referTime ) referTimeStr " +
            "FROM report a " +
            "JOIN ( SELECT * FROM v_academy_user WHERE 1=1 AND approvalType = 2 ${where} ) b ON a.id = b.approvalId " +
            "JOIN user_base_info u on u.number = b.number " +
            "ORDER BY status,a.createTime desc  LIMIT #{index},#{size}")
    List<JSONObject> getAcademyPage(@Param("where") String where,
                                    @Param("index") int index,
                                    @Param("size") int size);

    @Select("select count(1) total from report a " +
            "JOIN ( SELECT * FROM v_academy_user WHERE 1=1 AND approvalType = 2 ${where} ) b " +
            "ON a.id = b.approvalId where a.isDel = 0 ")
    JSONObject getAcademyPageCount(@Param("where") String where);

    @Select("SELECT u.NAME userName, a.*, b.stage approvalStage,status, result, opinion, approverNumber, approvalType, " +
            "b.id AS approvalTableId, FROM_UNIXTIME( referTime, '%Y-%m-%d' ) referTimeStr " +
            "FROM report a JOIN ( SELECT * FROM v_link_user_report WHERE 1 = 1 ${where} ) b ON a.id = b.approvalId " +
            "JOIN user_base_info u ON u.number = b.number " +
            "ORDER BY status, a.createTime desc LIMIT #{index},#{size}")
    List<JSONObject> getPersonPage(@Param("where") String where,
                                   @Param("index") int index,
                                   @Param("size") int size);

    @Select("select count(1) total from report a " +
            "JOIN ( SELECT * FROM v_link_user_report WHERE 1=1 ${where} ) b " +
            "ON a.id = b.approvalId where a.isDel = 0 ")
    JSONObject getPersonPageCount(@Param("where") String where);


    @Select("SELECT u.name userName,a.id,a.type,proposerNumber,fileURL,referTime,FROM_UNIXTIME(a.referTime) referTimeStr,count,a.stage ,b.status,b.result," +
            "b.id approvalTableId ,b.opinion ,b.stage approvalStage,b.approverNumber " +
            "FROM report a, approval b , user_base_info u " +
            "where a.isDel = 0 and b.isDel = 0 and b.isConfirm=2 and u.isDel = 0 and a.id = b.approvalId ${where} " +
            "and u.number = b.number and a.proposerNumber = u.number " +
            "ORDER BY status,a.createTime desc LIMIT #{index},#{size} ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from report a, approval b, user_base_info u " +
            "where a.isDel = 0 and b.isDel = 0 and u.isDel = 0 and a.id = b.approvalId " +
            "and u.number = b.number and a.proposerNumber = u.number ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM report a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT MAX(`count`) from report where isDel = 0 ${where} ")
    Integer queryMaxCount(@Param("where") String where);


    /*************** PC **************/
    //获取pc端用户的个人思想汇报
   /* @Select("select count(1) total from report a LEFT JOIN approval b " +
            "ON a.isDel = 0 and b.isDel=0 and a.id = b.approvalId ${where} ")*/


    @Select("select count(1) total from report a LEFT JOIN approval b " +
            "ON a.isDel = 0 and b.isDel=0 and a.id = b.approvalId and proposerNumber = ${number} ")
    JSONObject getUserPageCount(@Param("number") String number);

    /*@Select("SELECT r.id,proposerNumber,count,FROM_UNIXTIME(referTime) referTimeStr,a.`status`,a.result,approvalStage FROM report r " +
            "JOIN (select  MAX(stage) approvalStage,status,result,number,approvalId,isDel,approvalType" +
            "    FROM approval GROUP BY approvalId HAVING  approvalType = 2 AND isDel = 0) a " +
            "ON r.isDel = 0 and r.id = a.approvalId and r.proposerNumber=a.number ${where} ORDER BY count desc " +
            "LIMIT #{index},#{size} ")*/
    @Select("SELECT r.id,proposerNumber,count,FROM_UNIXTIME(referTime) referTimeStr,a.`status`,a.result,approvalStage FROM report r " +
            "JOIN (select stage approvalStage,status,result,number,approvalId,isDel,approvalType from (select * FROM approval " +
            "where approvalType = 2 and isDel = 0 and isConfirm=2 and number = ${number} order by approvalId,stage desc limit 10000000000  )b GROUP BY b.approvalId) a " +
            "ON r.isDel = 0 and r.id = a.approvalId and r.proposerNumber=a.number  ORDER BY count desc " +
            "LIMIT #{index},#{size} ")

    List<JSONObject> getUserPage(@Param("number") String number,
                                 @Param("index") int index,
                                 @Param("size") int size);


    @Select("SELECT u.name userName,b.id,b.type,proposerNumber,fileURL,referTime,FROM_UNIXTIME(b.referTime) referTimeStr,count,b.stage ,c.status,c.result, " +
            "c.id approvalTableId ,c.opinion ,c.stage approvalStage,c.approverNumber " +
            "FROM report b " +
            "JOIN (select id from report where id = ${id} )a on a.id = b.id  " +
            "left JOIN ( SELECT * FROM approval WHERE isDel = 0 and stage =  ${stage}  ) c ON  c.approvalId =b.id " +
            "left JOIN user_base_info u on u.number = c.number ")
    JSONObject getById(@Param("id") String id, @Param("stage") int stage);

}