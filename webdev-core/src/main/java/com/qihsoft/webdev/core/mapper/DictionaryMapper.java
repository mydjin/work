package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.Dictionary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qihsoft on 2018/08/10
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    @Select("SELECT a.* FROM dictionary a " +
            "JOIN (SELECT id from dictionary where isDel = 0 ${where} " +
            ")b ON a.id=b.id order by a.rank asc LIMIT #{index}, #{size}")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from dictionary where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM dictionary a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}