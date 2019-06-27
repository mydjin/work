package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.SysUserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * Created by Qihsoft on 2019/02/02
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {


    @Select("SELECT a.* " +
            "FROM sys_user_info a " +
            "JOIN (SELECT id from sys_user_info where isDel = 0 ${where} " +
            "LIMIT #{index}, #{size})b ON a.id=b.id order by a.createTime asc ")
    List<JSONObject> getPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);

    @Select("select count(1) total from sys_user_info where isDel = 0 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM sys_user_info a where 1=1 and isDel=0 ${where}  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);
}