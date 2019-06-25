package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Chapter;

import java.util.List;
/**
 * Created by party on 2018/08/28
 */
public interface ChapterMapper extends BaseMapper<Chapter> {
    //根据页数获取数据
    @Select("SELECT a.* " + 
              "FROM chapter a " + 
            "JOIN (SELECT id from chapter where isDel = 0 ${where} " + 
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size);

    //获取页数
    @Select("select count(1) total from chapter where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where);


    //获取全部数据
    @Select("SELECT a.* FROM chapter a where 1=1 and isDel=0 ${where}  order by courseId,sort")
    List<JSONObject> query(@Param("where") String where);


    //获取id,name
    @Select("SELECT a.id,a.name FROM chapter a where 1=1 and isDel=0 ${where}  order by courseId,sort")
    List<JSONObject> queryIdName(@Param("where") String where);


    //获取用户进度
    @Select( "SELECT a.id,a.name,a.content ,c.learnPercent,c.testScore,c.isFinish "+
            " FROM chapter a "+
            " JOIN (SELECT id FROM chapter WHERE isDel = 0 ${where}) b on a.id = b.id    " +
            " LEFT JOIN chapter_user c on c.chapterCode = a.id and c.number = ${number} "+
            "order by a.courseId,a.sort"
    )
    List<JSONObject> query_user(@Param("where") String where,@Param("number") String number);

    //根据ID获取对象
    @Select("SELECT a.* FROM chapter a where 1=1 and isDel=0 and id=${id} ")
    JSONObject queryById(@Param("id") String id);


}