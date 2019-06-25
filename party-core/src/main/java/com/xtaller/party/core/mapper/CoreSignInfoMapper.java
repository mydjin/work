package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreSignInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/12/13
*/
public interface CoreSignInfoMapper extends BaseMapper<CoreSignInfo> {
    @Select("select * from  core_sign_info where 1=1 ${where}")
    List<JSONObject> getSignInfoByWhere(@Param("where")  String where);

    @Select("select * from v_sign_info where isDel=0 ${where}")
    List<JSONObject> getVSignInfoByWhere(@Param("where")  String where);

}