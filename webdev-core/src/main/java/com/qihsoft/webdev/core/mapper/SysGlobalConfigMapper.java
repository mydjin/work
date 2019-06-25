package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysGlobalConfig;
import org.apache.ibatis.annotations.Update;

/**
* Created by Qihsoft on 2017/10/08
*/
public interface SysGlobalConfigMapper extends BaseMapper<SysGlobalConfig> {
    @Update("delete from sys_global_config where isDel=0 ")
    Boolean deleteBySystemId();
}