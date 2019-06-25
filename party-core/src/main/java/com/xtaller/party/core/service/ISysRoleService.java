package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;

import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysRole;

import java.util.List;

/**
* Created by Taller on 2017/09/25
*/
public interface ISysRoleService {
   /******************* CURD ********************/ 
   // 创建
   SysRole createRole(SysRole model);
   // 删除
   Boolean deleteRole(Object ids, String reviser);
   // 修改
   SysRole updateRole(SysRole model);
   // 查询
   List<SysRole> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w);
   // 查询一个id是否存在
   Boolean existId(Object id);
   /******************* CURD ********************/

   void initDefault(String systemId);
   SysRole getDefaultRole(String systemId);
   List<JSONObject> getAuthTreeByRoleId(String roleId);
   List<JSONObject> getAuthInfoByRoleId(String roleId);
   Boolean configRole(String roleId,List<JSONObject> menuList,List<JSONObject> btnsList);

   List<JSONObject> getRoleInfo();


}