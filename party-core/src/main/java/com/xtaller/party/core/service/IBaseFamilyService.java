package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.BaseFamily;

/**
* Created by Taller on 2017/11/08
*/
public interface IBaseFamilyService {
   /******************* CURD ********************/ 
   // 创建
   BaseFamily createBaseFamily(BaseFamily model);
   // 删除
   Boolean deleteBaseFamily(Object ids,String reviser);
   // 修改
   BaseFamily updateBaseFamily(BaseFamily model); 
   // 查询
   List<BaseFamily> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}