package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysRoleMenu;

import java.util.List;

/**
* Created by Taller on 2017/09/25
*/
public interface ISysRoleMenuService {
   /******************* CURD ********************/ 
   // 创建
   SysRoleMenu createRoleMenu(SysRoleMenu model);
   // 删除
   Boolean deleteRoleMenu(Object ids, String reviser);
   // 修改
   SysRoleMenu updateRoleMenu(SysRoleMenu model);
   // 查询
   List<SysRoleMenu> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/
   //查询列表信息
   List<JSONObject> queryRoleMenus(String roleId);
 
}