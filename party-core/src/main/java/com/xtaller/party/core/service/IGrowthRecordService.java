package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.GrowthRecord;

/**
* Created by Taller on 2018/08/20
*/
public interface IGrowthRecordService {
   /******************* CURD ********************/ 
   // 创建 
   GrowthRecord createGrowthRecord(GrowthRecord model); 
   // 删除 
   Boolean deleteGrowthRecord(Object ids,String reviser);
   // 修改 
   GrowthRecord updateGrowthRecord(GrowthRecord model); 
   // 查询 
   List<GrowthRecord> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}