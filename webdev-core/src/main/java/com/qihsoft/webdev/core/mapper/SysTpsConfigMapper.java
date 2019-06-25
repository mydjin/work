package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysTpsConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Qihsoft on 2017/10/06
*/
public interface SysTpsConfigMapper extends BaseMapper<SysTpsConfig> {
    @Update("delete from sys_tps_config where isDel=0 and code=#{code}")
    Boolean deleteByCode(@Param("code") String code);
}