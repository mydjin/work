package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Options;

/**
* Created by Taller on 2018/08/23
*/
public interface IOptionsService {
   /******************* CURD ********************/ 
   // 创建 
   Options createOptions(Options model); 
   // 删除 
   Boolean deleteOptions(Object ids,String reviser);
   // 修改 
   Options updateOptions(Options model); 
   // 查询 
   List<Options> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}