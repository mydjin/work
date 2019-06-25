package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.SurveyRecordChat;

import java.util.List;
/**
 * Created by party on 2018/08/15
 */
public interface SurveyRecordChatMapper extends BaseMapper<SurveyRecordChat> {


    @Select("SELECT * , " +
            "FROM_UNIXTIME(surveyTime) surveyTimeStr  " +
            "from v_survey_record_chat "+
            "WHERE 1=1  ${where} " +
            " ORDER BY surveyTime desc LIMIT  #{index}, #{size}")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from v_survey_record_chat where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM v_survey_record_chat a where 1=1 ${where}  order by surveyTime desc")
    List<JSONObject> queryAll(@Param("where") String where);


    @Select("SELECT a.*,FROM_UNIXTIME(surveyTime) surveyTimeStr FROM v_survey_record_chat a where 1=1 and a.id =${id}  order by surveyTime desc")
    List<JSONObject> queryById(@Param("id") String id);


    @Select("SELECT MAX(`count`) from survey_record_chat where isDel = 0 ${where} ")
    Integer queryMaxCount(@Param("where") String where);

    @Select("SELECT a.*,FROM_UNIXTIME(surveyTime) surveyTimeStr FROM v_survey_record_chat a where 1=1 and a.number =${number}  order by surveyTime desc")
    List<JSONObject> queryByNumber(@Param("number") String number);

}