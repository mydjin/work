package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Video;

import java.util.List;
/**
 * Created by party on 2018/08/29
 */
public interface VideoMapper extends BaseMapper<Video> {

    @Select("SELECT a.* " + 
              "FROM v_sign_video a " +
            "JOIN (SELECT id from v_sign_video where isDel = 0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ") 
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from v_sign_video where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM v_sign_video a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.url FROM v_sign_video a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> getURL(@Param("where") String where);

    @Select("SELECT a.id,a.url,a.code,a.path  FROM v_sign_video a where 1=1 and isDel=0 and a.code= '${code}' ")
    List<JSONObject> checkVideo(@Param("code") String code);

    @Select("DELETE FROM video WHERE sectionId =${sectionId}")
    String deleteBySec(@Param("sectionId") String sectionId);


}