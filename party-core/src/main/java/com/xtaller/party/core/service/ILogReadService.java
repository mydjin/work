package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.LogRead;

/**
* Created by Taller on 2018/01/05
*/
public interface ILogReadService {
   /******************* CURD ********************/ 
   // 创建 
   LogRead createLogRead(LogRead model);
   // 删除 
   Boolean deleteLogRead(Object ids,String reviser);
   // 修改 
   LogRead updateLogRead(LogRead model); 
   // 查询 
   List<LogRead> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}