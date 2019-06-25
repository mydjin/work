package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.ArticleType;

import java.util.List;
/**
 * Created by party on 2018/12/08
 */
public interface ArticleTypeMapper extends BaseMapper<ArticleType> {

    @Select("SELECT a.* " + 
              "FROM article_type a " + 
            "JOIN (SELECT id from article_type where isDel = 0 ${where} " + 
            ")b ON a.id=b.id order by a.createTime asc LIMIT #{index}, #{size}")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from article_type where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM article_type a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}