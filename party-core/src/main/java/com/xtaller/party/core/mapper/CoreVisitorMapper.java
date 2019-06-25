package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreVisitor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/01/02
*/
public interface CoreVisitorMapper extends BaseMapper<CoreVisitor> {
    @Select("SELECT a.* FROM core_party a " +
            "JOIN (SELECT id from core_party where isDel=0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime desc")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from core_party where isDel=0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);
}