package com.xtaller.party.core.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.BaseClass;

/**
* Created by Taller on 2017/11/08
*/
public interface IBaseClassService {
   /******************* CURD ********************/ 
   // 创建
   JSONObject createBaseClass(BaseClass model);
   // 删除
   Boolean deleteBaseClass(Object ids,String reviser);
   // 修改
   JSONObject updateBaseClass(BaseClass model);
   // 查询
   List<BaseClass> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/
   // 分页
   Page page(int index, int size, String where);
}