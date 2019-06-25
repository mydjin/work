package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SectionUser;

/**
* Created by Taller on 2018/11/05
*/
public interface ISectionUserService {
   /******************* CURD ********************/ 
   // 创建 
   SectionUser createSectionUser(SectionUser model); 
   // 删除 
   Boolean deleteSectionUser(Object ids,String reviser);
   // 修改 
   SectionUser updateSectionUser(SectionUser model); 
   // 查询 
   List<SectionUser> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}