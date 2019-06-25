package com.qihsoft.webdev.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qihsoft.webdev.core.model.SysMenu;
import com.qihsoft.webdev.core.model.SysMenuAuth;
import com.qihsoft.webdev.core.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/08
*/
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("SELECT a.id, a.loginName, a.`status`, a.number, a.phone,a.type,sr.name ," +
            "ubi.name realName, a.createTime regTime," +
            "IFNULL(c.loginTime,0) loginTime,IFNULL(c.token,'') token,c.loginServer,c.loginType " +
            "FROM sys_user a  " +
            "JOIN (select id from sys_user where isDel=0 " +
            " ${where} ORDER BY createTime desc " +
            "limit #{index}, #{size}) b  " +
            "ON a.id = b.id " +
            "left JOIN v_lastest_login_token c on a.id=c.userId  " +
            "JOIN sys_user_role sur ON a.id = sur.userId " +
            "JOIN sys_role sr ON sur.roleId = sr.id " +
            "JOIN v_user_info ubi ON ubi.number = a.number ")
    List<JSONObject> getPage( @Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from sys_user where isDel=0 ${where} ORDER BY createTime desc")
    JSONObject getPageCount(@Param("where") String where);


    @Select("SELECT a.* FROM sys_menu a " +
            "INNER JOIN sys_role_menu b on a.id = b.menuId " +
            "INNER JOIN sys_user_role c on b.roleId=c.roleId and c.userId=#{userId} " +
            "where a.isDel=0 " +
            "order by a.sort asc")
    List<SysMenu> getRoleMenuByUserId(@Param("userId") String userId);

    @Select("SELECT d.* FROM sys_menu_auth d  " +
            "INNER JOIN sys_menu a on d.menuId=a.id " +
            "INNER JOIN sys_role_menu b on a.id=b.menuId " +
            "INNER JOIN sys_user_role c on b.roleId=c.roleId and c.userId=#{userId} " +
            "where d.isDel=0 " +
            "order by d.sort asc")
    List<SysMenuAuth> getRoleAuthByUserId(@Param("userId") String userId);
}