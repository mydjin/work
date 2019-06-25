package com.qihsoft.webdev.core.service;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.SysUser;
import com.qihsoft.webdev.utils.bean.Page;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/08
*/
public interface ISysUserService {
    // 创建用户
    Object createUser(JSONObject object);
    // 修改用户信息
    Object updateUser(JSONObject object);

    SysUser updateSysUser(SysUser model);
    // 根据用户名读取信息
    Object findByLoginName(String loginName);
    // 检查登录名是否被登记
    //
    List<SysUser> findByIds(Object ids);
    Boolean existLoginName(String loginName);
    // 分页
    Page page(int index, int size, String where);
    // id是否存在
    Boolean existId(Object id);
    Boolean deleteUser(Object ids, String reviser);

    Boolean initPwd(JSONObject object);
    // 获取当前用户权限
    List<JSONObject> getUserAuth(String userId);


}