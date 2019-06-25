package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Template;

/**
* Created by Taller on 2018/08/10
*/
public interface ITemplateService {
   /******************* CURD ********************/ 
   // 创建 
   Template createTemplate(Template model); 
   // 删除 
   Boolean deleteTemplate(Object ids,String reviser);
   // 修改 
   Template updateTemplate(Template model); 
   // 查询 
   List<Template> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}