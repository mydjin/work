package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.model.SysUser;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;

import java.util.List;

/**
* Created by Taller on 2017/09/08
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
    Page page(int index, int size, String where,String roleWhere,String infoWhere);
    // id是否存在
    Boolean existId(Object id);
    Boolean deleteUser(Object ids, String reviser);

    List<JSONObject> getSubSystemByUserId(String userId);

    Boolean initPwd(JSONObject object);
    // 获取当前用户权限
    List<JSONObject> getUserAuth(String userId);

    // 属于
    Boolean exist(List<Where> w);


}