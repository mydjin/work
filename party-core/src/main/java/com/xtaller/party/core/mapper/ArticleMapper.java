package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/06/10
*/
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("select count(1) total from article where 1=1 and isDel=0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

@Select("SELECT title,author,article.id,summary,source,pictureId,type,releaseStatus,auditingStatus,auditor,setTopStatus,hits,isNeedAuditing, " +
        "artworkURL,signURL,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
        " FROM article left join v_sign_picture on 1=1 and article.pictureId=v_sign_picture.id  where article.isdel=0 ${where} " +
        " order by releaseTime desc LIMIT ${index}, ${size}")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select("SELECT title,article.id FROM article  where isdel=0  order by releaseTime desc,article.id ")
    List<JSONObject> getIdName();




    @Select("SELECT title,author,a.id,summary,source,pictureId,type,releaseStatus,auditingStatus,auditor,setTopStatus,hits,isNeedAuditing,content, " +
            "artworkURL,signURL,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
            " FROM article a join(  SELECT id from article where isdel=0 and id =  ${id}    )b  on a.id = b.id  left join v_sign_picture on 1=1 and a.pictureId=v_sign_picture.id    "
    )
    JSONObject getInfo(@Param("id") String id);


    //    分页列表参数：页码、每页记录数、标题关键字
    //    返回：标题、发布时间、作者、文章类型、摘要、id、来源

    @Select("SELECT title,author,article.id,summary,source,pictureId,artworkURL,signURL,hits,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
            " FROM article left join v_sign_picture on 1=1 and article.pictureId=v_sign_picture.id ${where} where article.isDel=0 and releaseStatus=2 " +
            " order by releaseTime desc,article.id  LIMIT ${index}, ${size}")
    List<JSONObject> getList(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);


    @Select("SELECT a.*,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
            " FROM article a where 1=1 and isDel=0  ${where}  order by releaseTime desc")
    List<JSONObject> queryAll(@Param("where") String where);


    //12.02
    @Select("select a.id, a.title,a.releaseTime,a.hits,b.name,b.artworkURL,b.thumbnailURL,signURL " +
            "            from notice a,v_sign_picture b " +
            "            where 1=1  and a.isDel=0 and  a.id=b.tableId " +
            "            order by releaseTime desc")
    List<JSONObject> WqueryAll(@Param("where") String where);

//获取详情页的图片
    @Select("select a.id, b.name,b.artworkURL,b.thumbnailURL,signURL "+
            "from notice a,v_sign_picture b "+
            "where 1=1  and a.isDel=0 and  a.id=b.tableId "+
            " order by a.createTime desc")
    List<JSONObject> getPicture(@Param("where") String where);

    @Select("select hits from ${table} where id = ${id} ")
   int getHits(@Param("table") String table,@Param("id") String id);


    @Select("update ${table} set hits = ${hits} where id = ${id} ")
    void setHits(@Param("table") String table,@Param("id") String id,@Param("hits") int hits);


    //微信端分页查询
    @Select("SELECT title,article.id,pictureId,artworkURL,signURL,hits,thumbnailURL,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
            "           FROM article left join v_sign_picture on 1=1 and article.pictureId=v_sign_picture.id ${where} where article.isDel=0 and releaseStatus=2 " +
            "          order by releaseTime desc,article.id  LIMIT ${index}, ${size}")
    List<JSONObject> WgetList(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

}

