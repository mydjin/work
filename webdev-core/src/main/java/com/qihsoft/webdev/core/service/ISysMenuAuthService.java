package com.qihsoft.webdev.core.service;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.SysMenuAuth;
import com.qihsoft.webdev.utils.bean.Where;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/18
*/
public interface ISysMenuAuthService {
   /******************* CURD ********************/ 
   // 创建
   SysMenuAuth createMenuAuth(SysMenuAuth model);
   // 删除
   Boolean deleteMenuAuth(Object ids, String reviser);
   // 修改
   SysMenuAuth updateMenuAuth(SysMenuAuth model);
   // 查询
   List<SysMenuAuth> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w);
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/
   //查询列表信息
   List<JSONObject> queryList(String where);
   //查询权限信息
   List<JSONObject> queryAuths(String where);
}