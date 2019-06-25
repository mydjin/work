package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.BaseGrade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/11/08
*/
public interface BaseGradeMapper extends BaseMapper<BaseGrade> {
    @Select("SELECT a.* FROM v_grade a " +
            "JOIN (SELECT id from v_grade where 1=1 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by `name` desc,type,graduation")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from v_grade where 1=1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("select * from v_grade where id=#{id} ")
    JSONObject getById(@Param("id") String id);


    @Select("select ${feilds} from v_grade where 1=1 ${where}  ")
    List<JSONObject> getList(@Param("feilds") String feilds,@Param("where") String where);
}