package com.xtaller.party.core.service;

import com.xtaller.party.core.model.SysAutoCode;
import com.xtaller.party.utils.bean.Where;


import java.util.List;

/**
* Created by Taller on 2017/09/12
*/
public interface ISysAutoCodeService {
   /******************* CURD ********************/ 
   // 创建
   SysAutoCode createAutoCode(SysAutoCode model);
   // 删除
   Boolean deleteAutoCode(Object ids, String reviser);
   // 修改
   SysAutoCode updateAutoCode(SysAutoCode model);
   // 查询
   List<SysAutoCode> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   /******************* CURD ********************/ 
 
}