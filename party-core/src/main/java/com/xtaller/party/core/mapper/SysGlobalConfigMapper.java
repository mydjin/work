package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.SysGlobalConfig;
import org.apache.ibatis.annotations.Update;

/**
* Created by Taller on 2017/10/08
*/
public interface SysGlobalConfigMapper extends BaseMapper<SysGlobalConfig> {
    @Update("delete from sys_global_config where isDel=0 ")
    Boolean deleteBySystemId();
}