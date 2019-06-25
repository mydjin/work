package com.qihsoft.webdev.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.Picture;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by party on 2018/08/23
 */
public interface PictureMapper extends BaseMapper<Picture> {

    @Select("SELECT a.* FROM v_sign_picture a " +
            "JOIN (SELECT id from v_sign_picture where isDel = 0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size); 

    @Select("select count(1) total from v_sign_picture where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM v_sign_picture a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.artworkURL FROM v_sign_picture a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> getPictureURL(@Param("where") String where);


    @Select("SELECT a.id,a.artworkURL,a.code  FROM v_sign_picture a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> checkPicture(@Param("where") String where);


}