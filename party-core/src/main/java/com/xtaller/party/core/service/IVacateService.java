package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Vacate;

/**
* Created by Taller on 2018/08/15
*/
public interface IVacateService {
   /******************* CURD ********************/ 
   // 创建 
   Vacate createVacate(Vacate model); 
   // 删除 
   Boolean deleteVacate(Object ids,String reviser);
   // 修改 
   Vacate updateVacate(Vacate model); 
   // 查询 
   List<Vacate> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}