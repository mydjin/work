package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Application;

import java.util.List;

/**
 * Created by party on 2018/08/13
 */
public interface ApplicationMapper extends BaseMapper<Application> {

    @Select("SELECT  u.name userName,a.*," +
            "b.stage,status,result,opinion,approverNumber,approvalType,b.id As approvalTableId, " +
            "FROM_UNIXTIME( referTime, '%Y-%m-%d' ) referTimeStr " +
            "FROM application a " +
            "JOIN ( SELECT * FROM approval WHERE isDel = 0 and isConfirm = 2 ${where} ) b ON a.id = b.approvalId " +
            "JOIN user_base_info u on u.number = b.number " +
            "ORDER BY status,a.createTime desc  LIMIT #{index},#{size}  ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select("SELECT  u.name userName,a.*," +
            "c.stage,status,result,opinion,approverNumber,approvalType,c.id As approvalTableId, " +
            "FROM_UNIXTIME( referTime, '%Y-%m-%d' ) referTimeStr " +
            "FROM application a " +
            "JOIN ( SELECT id FROM application WHERE isDel = 0 and id = ${id} ) b ON a.id = b.id " +
            "left JOIN ( SELECT * FROM approval WHERE isDel = 0 and isConfirm = 2 and stage =  ${stage}  ) c ON  c.approvalId =b.id " +
            "left JOIN user_base_info u on u.number = c.number ")
    JSONObject getById(@Param("id") String id,@Param("stage") int stage);

    @Select("select count(1) total from application a " +
            "JOIN ( SELECT * FROM approval WHERE isDel = 0 and isConfirm = 2 ${where} ) b " +
            "ON a.id = b.approvalId where a.isDel = 0 ")
    JSONObject getPageCount(@Param("where") String where);


    @Select("SELECT  u.name userName,a.*," +
            "b.stage,status,result,opinion,approverNumber,approvalType,b.id As approvalTableId, " +
            "FROM_UNIXTIME( referTime, '%Y-%m-%d' ) referTimeStr " +
            "FROM application a " +
            "JOIN ( SELECT * FROM v_academy_user WHERE 1=1  AND approvalType = 1 ${where} ) b ON a.id = b.approvalId " +
            "JOIN user_base_info u on u.number = b.number " +
            "ORDER BY status,a.createTime desc  LIMIT #{index},#{size}")
    List<JSONObject> getAcademyPage(@Param("where") String where,
                                    @Param("index") int index,
                                    @Param("size") int size);

    @Select("select count(1) total from application a " +
            "JOIN ( SELECT * FROM v_academy_user WHERE 1=1 AND approvalType = 1 ${where} ) b " +
            "ON a.id = b.approvalId where a.isDel = 0 ")
    JSONObject getAcademyPageCount(@Param("where") String where);


    @Select("SELECT  u.name userName,a.*," +
            "b.stage,status,result,opinion,approverNumber,approvalType,b.id As approvalTableId, " +
            "FROM_UNIXTIME( referTime, '%Y-%m-%d' ) referTimeStr " +
            "FROM application a " +
            "JOIN ( SELECT * FROM v_link_user_application WHERE 1=1 ${where} ) b ON a.id = b.approvalId " +
            " JOIN user_base_info u on u.number = b.number " +
            "ORDER BY status,a.createTime desc   LIMIT #{index},#{size}")
    List<JSONObject> getPersonPage(@Param("where") String where,
                                   @Param("index") int index,
                                   @Param("size") int size);

    @Select("select count(1) total from application a " +
            "JOIN ( SELECT * FROM v_link_user_application WHERE 1=1 ${where} ) b " +
            "ON a.id = b.approvalId where a.isDel = 0 ")
    JSONObject getPersonPageCount(@Param("where") String where);


    @Select("SELECT a.* FROM application a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.* FROM application a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<Application> queryByNumber(@Param("where") String where);
}