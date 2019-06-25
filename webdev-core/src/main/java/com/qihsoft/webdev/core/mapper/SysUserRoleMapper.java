package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Qihsoft on 2017/09/25
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    @Update("delete from sys_user_role where isDel=0 " +
            "and userId=#{userId} ")
    Boolean deleteByUserIdAndSystemId(@Param("userId") String userId);
}