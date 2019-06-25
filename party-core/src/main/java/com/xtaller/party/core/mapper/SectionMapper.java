package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Section;

import java.util.List;
/**
 * Created by party on 2018/08/29
 */
public interface SectionMapper extends BaseMapper<Section> {

    //根据页数获取数据
    @Select("SELECT a.* " + 
              "FROM section a " + 
            "JOIN (SELECT id from section where isDel = 0 ${where} " + 
            "order by a.createTime asc LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc  ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    //获取页数
    @Select("select count(1) total from section where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 


    //查找符合条件的内容
    @Select("SELECT * FROM section where 1=1 and isDel=0 ${where}  order by chapterId,sort")
    List<JSONObject> query(@Param("where") String where);

    //查找IDNAME
    @Select("SELECT id,name FROM section where 1=1 and isDel=0 ${where}  order by chapterId,sort")
    List<JSONObject> queryIdName(@Param("where") String where);

    //查找进度
    @Select( "SELECT a.id,a.name ,c.learnPercent,c.testScore,c.isFinish "+
            " FROM section a "+
            " JOIN (SELECT id FROM section WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            " LEFT JOIN section_user c on c.sectionCode = a.id and c.number = ${number} "+
           " order by a.chapterId,a.sort"
    )
    List<JSONObject> query_user(@Param("where") String where,@Param("number") String number);

   //查找课程ID
    @Select( " SELECT a.id from online_course a where id = (SELECT b.courseId from chapter b where id = (SELECT c.chapterId from section c where id =${id}))"
    )
    JSONObject query_course(@Param("id") String id);

    //根据ID获取内容
    @Select("SELECT a.* FROM section a where 1=1 and isDel=0 and id=${id} ")
    JSONObject queryById(@Param("id") String id);

    //根据ID获取类型
    @Select("SELECT a.type FROM section a where 1=1 and isDel=0 and id=${id} ")
    JSONObject queryTypeById(@Param("id") String id);


    //根据ID获取内容和URL
    @Select( "SELECT a.*  ,c.url,signURL "+
            " FROM section a "+
            " JOIN (SELECT id FROM section WHERE isDel = 0 and id=${id} ) b on a.id = b.id    " +
            " LEFT JOIN v_sign_video c on c.sectionId = a.id  "
    )
    JSONObject queryById_url(@Param("id") String id);

   //根据ID获取URL
    @Select( "SELECT c.url,signURL "+
            " FROM section a "+
            " JOIN (SELECT id FROM section WHERE isDel = 0 and id=${id} ) b on a.id = b.id    " +
            " LEFT JOIN v_sign_video c on c.sectionId = a.id  "
    )
    JSONObject queryUrlById(@Param("id") String id);

    //根据ID获取导航栏
    @Select("  SELECT  a.id,a.name,b.id chaperId,b.name chaperName,b.courseId,c.title courseName  from chapter b INNER JOIN" +
            "(SELECT id,name,chapterId" +
            "    FROM section where 1=1 and isDel=0 and id=${id})a on b.id=a.chapterId" +
            "    left join online_course c on b.courseId =c.id ")
    JSONObject queryBarById(@Param("id") String id);




}