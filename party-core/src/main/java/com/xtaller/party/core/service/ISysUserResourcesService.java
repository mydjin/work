package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;

import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysUserResources;

import java.util.List;

/**
* Created by Taller on 2017/10/08
*/
public interface ISysUserResourcesService {
   /******************* CURD ********************/ 
   // 创建
   SysUserResources createUserResources(SysUserResources model);
   // 删除
   Boolean deleteUserResources(Object ids, String reviser);
   // 修改
   SysUserResources updateUserResources(SysUserResources model);
   // 查询
   List<SysUserResources> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w);
   // 查询一个id是否存在
   Boolean existId(Object id);
   /******************* CURD ********************/

   JSONObject getUserResources(String systemId, String userId);

   Page page(int index, int size, String parentId, String where);


   Boolean createFile(SysUserResources model);
   Boolean deleteFile(Object ids, String systemId, String userId, int total);


}