package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Dictionary;

/**
* Created by Taller on 2018/08/10
*/
public interface IDictionaryService {
   /******************* CURD ********************/ 
   // 创建 
   Dictionary createDictionary(Dictionary model); 
   // 删除 
   Boolean deleteDictionary(Object ids,String reviser);
   // 修改 
   Dictionary updateDictionary(Dictionary model); 
   // 查询 
   List<Dictionary> findByIds(Object ids);
   //查询byName
   List<Dictionary> findByName(Object name);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}