package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.MajorInfo;

/**
* Created by Taller on 2018/08/23
*/
public interface IMajorInfoService {
   /******************* CURD ********************/ 
   // 创建 
   MajorInfo createMajorInfo(MajorInfo model); 
   // 删除 
   Boolean deleteMajorInfo(Object ids,String reviser);
   // 修改 
   MajorInfo updateMajorInfo(MajorInfo model); 
   // 查询 
   List<MajorInfo> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}