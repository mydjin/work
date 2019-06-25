package com.qihsoft.webdev.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/25
*/
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    //查询目录
    @Select("SELECT menuId  FROM sys_role_menu  where 1=1 and isDel=0 and roleId= ${roleId}  ")
    List<JSONObject> queryRoleMenus(@Param("roleId") String roleId);
}