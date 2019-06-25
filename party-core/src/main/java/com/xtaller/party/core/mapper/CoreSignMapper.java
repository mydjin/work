package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreSign;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/12/13
*/
public interface CoreSignMapper extends BaseMapper<CoreSign> {
    @Select("select * from v_sign where 1=1 ${where}")
    List<JSONObject> getSignByWhere(@Param("where") String where);
}