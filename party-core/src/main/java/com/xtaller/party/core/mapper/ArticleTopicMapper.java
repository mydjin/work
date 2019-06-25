package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.ArticleTopic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/06/10
*/
public interface ArticleTopicMapper extends BaseMapper<ArticleTopic> {
    @Select("select count(1) total from article_topic where 1=1  ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM article_topic a " +
            "JOIN (SELECT id from article_topic where  1=1   ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime desc ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

}