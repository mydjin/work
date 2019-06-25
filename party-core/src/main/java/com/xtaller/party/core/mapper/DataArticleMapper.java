package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.DataArticle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/11/24
*/
public interface DataArticleMapper extends BaseMapper<DataArticle> {
    @Select("SELECT a.* FROM v_article a " +
            "JOIN (SELECT id from v_article where 1=1 ${where} " +
            "LIMIT #{index}, #{size}) b ON a.id=b.id order by `top` desc")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from v_article where 0=0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("select * from v_article where id=#{id} ")
    JSONObject getById(@Param("id") String id);

    @Select("select * from data_article a LEFT JOIN v_article_accepter u on a.id=u.articleId  " +
            " where a.status = 1  and (a.type=6" +
            " or (a.type in(7,8,13) and u.userId=${userId} )" +
            ")  ${where}  order by a.createTime desc  LIMIT #{index}, #{size}")
    List<JSONObject> getMyNoticePage(@Param("where") String where,
            @Param("userId") String userId,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total  from data_article a LEFT JOIN v_article_accepter u on a.id=u.articleId  " +
            " where a.status = 1  and (a.type=6" +
            " or (a.type in(7,8,13) and u.userId=${userId} )" +
            ")  ${where}  ")
    JSONObject getMyNoticePageCount(@Param("where") String where,@Param("userId") String userId);

    @Select("SELECT a.* FROM data_article a " +
            "JOIN (SELECT id from data_article where 1=1 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by `top` desc")
    List<JSONObject> getMyCreateNoticesPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from data_article where 0=0 ${where} ")
    JSONObject getMyCreateNoticesPageCount(@Param("where") String where);

    @Select("select id,title,video,introduction,type,browse,top,zan,comment,status,creator,FROM_UNIXTIME(createtime , '%Y-%m-%d') as createTime,(select name" +
            " from base_teacher where base_teacher.id=data_article.creator) as creatorName from data_article where isdel = 0 and (type in (1,2,5,6,9,13) " +
            "or (type = 7 and id in(select articleId from data_article_accepter where objectId=${gradeId} and objectType=1)) " +
            "or (type = 8 and id in(select articleId from data_article_accepter where objectId=${classId}  and objectType=1)) " +
            "or (type=10 and gradeId=${gradeId}  ) " +
            "or (type in(4,11,12) and classId=${classId} ) " +
            " ) ${where}")
    List<JSONObject> getClassAritcle(@Param("where") String where,
                                            @Param("classId") String classId,
                                            @Param("gradeId") String gradeId);

    @Select("select id,title,video,introduction,type,browse,top,zan,comment,status,creator,createtime,(select name" +
            " from base_teacher where base_teacher.id=data_article.creator) as creatorName from data_article where isdel = 0 ${where}")
    List<JSONObject> getScreenArticle(@Param("where") String where);

    @Select("SELECT a.* FROM v_article a " +
            "JOIN (SELECT id from v_article where 1=1 ${where} " +
            "LIMIT #{index}, #{size}) b ON a.id=b.id order by ${orderField} desc")
    List<JSONObject> boutiquePage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size,
                            @Param("orderField") String orderField
    );
}