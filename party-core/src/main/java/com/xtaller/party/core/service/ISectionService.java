package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Section;

/**
* Created by Taller on 2018/08/29
*/
public interface ISectionService {
   /******************* CURD ********************/ 
   // 创建 
   Section createSection(Section model); 
   // 删除 
   Boolean deleteSection(Object ids,String reviser);
   // 修改 
   Section updateSection(Section model); 
   // 查询 
   List<Section> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}