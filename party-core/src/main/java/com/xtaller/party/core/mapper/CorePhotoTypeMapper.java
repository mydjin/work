package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CorePhotoType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/01/08
*/
public interface CorePhotoTypeMapper extends BaseMapper<CorePhotoType> {
    @Select("select * from v_photo_type where 1=1 ${where} ")
    List<JSONObject> getByKey(@Param("where") String where);
}