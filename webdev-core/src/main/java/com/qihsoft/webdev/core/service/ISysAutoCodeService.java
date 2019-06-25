package com.qihsoft.webdev.core.service;

import com.qihsoft.webdev.core.model.SysAutoCode;
import com.qihsoft.webdev.utils.bean.Where;


import java.util.List;

/**
* Created by Qihsoft on 2017/09/12
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