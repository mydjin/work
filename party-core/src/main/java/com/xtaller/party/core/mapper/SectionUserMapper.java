package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.SectionUser;

import java.util.List;
/**
 * Created by party on 2018/11/05
 */
public interface SectionUserMapper extends BaseMapper<SectionUser> {

    //通用
    @Select("SELECT a.* " + 
            ",FROM_UNIXTIME(lastLearnTime) lastLearnTimeStr  " +
              "FROM section_user a " + 
            "JOIN (SELECT id from section_user where isDel = 0 ${where} " + 
            "order by a.createTime asc LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc   ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from section_user where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* " +
            ",FROM_UNIXTIME(lastLearnTime) lastLearnTimeStr  " +
            "FROM section_user a " +
            "JOIN (SELECT id from section_user where isDel = 0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    //后台专用
    @Select("SELECT  *  from v_section_note where 1=1  ${where}  order by academyCode,majorCode,lastLearnTime asc  LIMIT #{index}, #{size}")
    List<JSONObject> getPage_admin(@Param("where") String where,
                                   @Param("index") int index,
                                   @Param("size") int size);


    @Select("SELECT  *  from v_section_note where 1=1  ${where}  order by academyCode,majorCode,lastLearnTime asc ")
    List<JSONObject> queryAll_admin(@Param("where") String where);



    //PC端专用
    @Select("SELECT  a.* from section_user a where isDel = 0 and number= ${number} and sectionCode= ${sectionCode} order by a.createTime desc")
    JSONObject queryUserNote(@Param("number") String number,@Param("sectionCode") String sectionCode);

    //微信端专用



}