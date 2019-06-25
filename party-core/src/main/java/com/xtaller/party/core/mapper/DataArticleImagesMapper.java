package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.DataArticleImages;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* Created by Taller on 2017/11/26
*/
public interface DataArticleImagesMapper extends BaseMapper<DataArticleImages> {
    @Update("delete from data_article_images where isDel=0 " +
            "and articleId=#{articleId} ")
    Boolean deleteImagesByArticleId(@Param("articleId") String articleId);

    @Select("select * from data_article_images where id in(select min(img.id) from data_article_images img, data_article a where a.type= #{type}" +
            " and img.articleid = a.id  GROUP BY a.id  ) order by `id` desc LIMIT 0,#{num} ")
    List<JSONObject> FindByArticleType(@Param("num") int num,@Param("type") int type);

    @Select("select m.*,a1.title from data_article_images m,data_article a1 where m.articleid=a1.id and m.isDel = 0 and a1.isDel = 0" +
            " and m.id in(select min(img.id) from data_article_images img, data_article a where a.type= #{type}" +
            " and img.articleid = a.id and a.isDel= 0 and img.isDel=0  GROUP BY a.id  ) order by m.id desc LIMIT 0,#{num} ")
    List<JSONObject> FindImgByArticleType(@Param("num") int num,@Param("type") int type);


    @Select("select m.*,a1.title,a1.zan,a1.createTime from data_article_images m,data_article a1 where m.articleid=a1.id and m.isDel = 0 and a1.isDel = 0" +
            " and m.id in(select min(img.id) from data_article_images img, data_article a where a.type in(1,2,3,4,9,11,13,14,15,17,18)" +
            " and img.articleid = a.id and a.isDel= 0 and img.isDel=0  GROUP BY a.id  ) order by ${orderField} desc LIMIT 0,#{num} ")
    List<JSONObject> findImgForBoutique(@Param("num") int num,@Param("orderField") String orderField);
}