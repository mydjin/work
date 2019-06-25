package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.NoticeReview;

import java.util.List;
/**
 * Created by party on 2018/08/23
 */
public interface NoticeReviewMapper extends BaseMapper<NoticeReview> {

    @Select("SELECT a.* , " +
            "FROM_UNIXTIME(reviewTime,'%Y-%m-%d') reviewTimeStr  " +
            "FROM notice_review a " +
            "JOIN (SELECT id from notice_review where isDel = 0 ${where} " + 
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from notice_review where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM notice_review a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}