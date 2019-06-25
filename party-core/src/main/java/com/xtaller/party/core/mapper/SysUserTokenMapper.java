package com.xtaller.party.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.SysUserToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Taller on 2017/09/08
*/
public interface SysUserTokenMapper extends BaseMapper<SysUserToken> {
    @Select("SELECT a.* FROM sys_user_token a where 1=1 and isDel=0 and type= '${type}' and token= '${token}' ")
    JSONObject queryUserIdByToken(@Param("type") String type,@Param("token") String token);
}