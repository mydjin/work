package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xtaller.party.core.model.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Taller on 2017/09/25
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    @Update("delete from sys_user_role where isDel=0 " +
            "and userId=#{userId} ")
    Boolean deleteByUserIdAndSystemId(@Param("userId") String userId);
}