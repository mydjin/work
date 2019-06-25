package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Notice;

import java.util.List;
/**
 * Created by party on 2018/08/15
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("SELECT a.* , c.artworkURL ,signURL, " +
            "FROM_UNIXTIME(releaseTime) releaseTimeStr  " +
            "FROM notice a " +
            "JOIN (SELECT id from notice where isDel = 0 ${where})b ON a.id=b.id " +
            "LEFT JOIN v_sign_picture c ON a.pictureId = c.id " +
            "order by releaseTime desc LIMIT #{index}, #{size} ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from notice where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where);

    //    分页列表参数：页码、每页记录数、标题关键字
    //    返回：标题、发布时间、作者、文章类型、摘要、id、来源
//    @Select("SELECT title,author,id,summary,source,pictureId,  " +
//            "FROM_UNIXTIME(releaseTime,'%Y-%m-%d') releaseTimeStr  " +
//            "FROM notice where  1=1 ${where} " +
//            " order by releaseTime desc LIMIT ${index}, ${size}")

    @Select("SELECT title,author,notice.id,summary,source,pictureId,hits,signURL, " +
            "artworkURL,FROM_UNIXTIME(releaseTime) releaseTimeStr " +
            " FROM notice left join v_sign_picture on 1=1 and notice.pictureId=v_sign_picture.id ${where} where isPublish=2 " +
            " order by releaseTime desc LIMIT ${index}, ${size}")
    List<JSONObject> getList(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("SELECT title,notice.id FROM notice where isPublish=2  order by releaseTime desc ")
    List<JSONObject> getIdName();


    @Select("SELECT a.* FROM notice a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    //    二、文章详情（通过id）
    //    返回：通知公告所有字段（去掉共有字段）
    @Select("select title,content,author,source,pictureId,hits,summary,likenum,  " +
            "FROM_UNIXTIME(releaseTime) as releaseTimeStr  " +
            "from notice where 1=1  ${where}  " +
            "order by releaseTime desc ")
    List<JSONObject> queryDetail(@Param("where") String where);

//获取详情页的图片
    @Select("select a.id, b.name,b.artworkURL,b.thumbnailURL,signURL  "+
            "from article a,v_sign_picture b "+
            "where 1=1  and a.isDel=0 and  a.id=b.tableId  ${where} "+
            " order by releaseTime desc")
    List<JSONObject> getPicture(@Param("where") String where);

}

