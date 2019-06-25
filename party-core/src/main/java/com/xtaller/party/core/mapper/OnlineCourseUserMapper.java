package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.OnlineCourseUser;

import java.util.List;
/**
 * Created by party on 2018/11/06
 */
public interface OnlineCourseUserMapper extends BaseMapper<OnlineCourseUser> {

    //通用
    @Select("SELECT a.* " +
            ",FROM_UNIXTIME(lastLearnTime) lastLearnTimeStr  " +
            "FROM online_course_user a " +
            "JOIN (SELECT id from online_course_user where isDel = 0 ${where} " + 
            "order by a.createTime asc LIMIT #{index}, #{size})b ON a.id=b.id  order by a.createTime asc   ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size);

    @Select("select count(1) total from online_course_user where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* " +
            ",FROM_UNIXTIME(lastLearnTime) lastLearnTimeStr  " +
            "FROM online_course_user a " +
            "JOIN (SELECT id from online_course_user where isDel = 0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);





    //后台专用
    @Select("SELECT  *  from v_course_note where 1=1  ${where}  order by academyCode,majorCode,lastLearnTime asc  LIMIT #{index}, #{size}")
    List<JSONObject> getPage_admin(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select("SELECT  *  from v_course_note where 1=1  ${where}  order by academyCode,majorCode,lastLearnTime asc ")
    List<JSONObject> queryAll_admin(@Param("where") String where);




    //PC端、微信端专用
    @Select("SELECT a.* FROM online_course_user a where 1=1 and isDel=0 and number= ${number} and courseId= ${courseId}  order by createTime desc")
    JSONObject queryUserNote(@Param("number") String number, @Param("courseId") String courseId);



    //fixme:暂时调整为实时统计进度，正式上线需考虑性能问题 lw 19.1.6
    //fixme ：代码变更：1、 count(c.id) -> nvl(sum(c.learnPercent)/100,0) 2、删掉and isFinish=2
    @Select("SELECT  IFNULL(sum(c.learnPercent)/100,0) from section_user c where number = ${number} and sectionCode in (select b.id from section b where  chapterId in  " +
            "(SELECT a.id FROM chapter a where courseId = ${courseId} and isDel =0) and isDel =0) and isDel =0 ")
    float   getPerSum(@Param("number") String number,@Param("courseId") String courseId);


    @Select("SELECT count(b.id)  from section b where  chapterId in  (SELECT a.id FROM chapter a where courseId =${courseId} and isDel =0) and isDel =0  ")
   float   getPerCount(@Param("courseId") String courseId);



}