package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysTpsConfig;

import java.util.List;

/**
* Created by Taller on 2017/10/06
*/
public interface ISysTpsConfigService {
   /******************* CURD ********************/ 
   // 创建
   SysTpsConfig createTpsConfig(SysTpsConfig model);
   // 删除
   Boolean deleteTpsConfig(Object ids, String reviser);
   // 修改
   SysTpsConfig updateTpsConfig(SysTpsConfig model);
   // 查询
   List<SysTpsConfig> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w);
   // 查询一个id是否存在
   Boolean existId(Object id);
   /******************* CURD ********************/

   Boolean config(String code, List<SysTpsConfig> models);
   JSONObject getByCode(String code);
 
}