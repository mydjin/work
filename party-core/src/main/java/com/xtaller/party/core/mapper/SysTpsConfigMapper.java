package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.SysTpsConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Taller on 2017/10/06
*/
public interface SysTpsConfigMapper extends BaseMapper<SysTpsConfig> {
    @Update("delete from sys_tps_config where isDel=0 and code=#{code}")
    Boolean deleteByCode(@Param("code") String code);
}