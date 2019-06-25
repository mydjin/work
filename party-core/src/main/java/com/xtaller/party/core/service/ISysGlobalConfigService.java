package com.xtaller.party.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysGlobalConfig;

import java.util.List;

/**
* Created by Taller on 2017/10/08
*/
public interface ISysGlobalConfigService {
   /******************* CURD ********************/ 
   // 创建
   SysGlobalConfig createGlobalConfig(SysGlobalConfig model);
   // 删除
   Boolean deleteGlobalConfig(Object ids, String reviser);
   // 修改
   SysGlobalConfig updateGlobalConfig(SysGlobalConfig model);
   // 查询
   List<SysGlobalConfig> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w);
   Boolean existSystemId(Object systemId);
   // 查询一个id是否存在
   Boolean existId(Object id);
   /******************* CURD ********************/

   Boolean config(List<SysGlobalConfig> models);
   JSONObject getSetting();
}