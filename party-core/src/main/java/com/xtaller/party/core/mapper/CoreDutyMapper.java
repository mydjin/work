package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.CoreDuty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2018/01/09
*/
public interface CoreDutyMapper extends BaseMapper<CoreDuty> {
    @Select("select *,FROM_UNIXTIME(core_duty.dutyDate , '%Y-%m-%d') as dateStr from core_duty where isDel=0 ${where} ")
    List<JSONObject> getByKey(@Param("where") String where);
}