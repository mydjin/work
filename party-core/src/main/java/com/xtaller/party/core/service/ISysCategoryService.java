package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysCategory;

import java.util.List;

/**
* Created by Taller on 2017/09/17
*/
public interface ISysCategoryService {
   /******************* CURD ********************/ 
   // 创建
   SysCategory createCategory(SysCategory model);
   // 删除
   Boolean deleteCategory(Object ids, String reviser);
   // 修改
   SysCategory updateCategory(SysCategory model);
   // 查询
   List<SysCategory> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/
   // 分页
   Page page(int index, int size, String code, String query);
   JSONObject getCategoryByCode(String code, int level);
   List<SysCategory> getCategoryByParentId(String parentId);
   List<SysCategory> getCategoryBySuperId(String superId);
   List<SysCategory> getSuperCategory();

   // 组织架构
   List<SysCategory> getOrganizational();
}