package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ExamRecord;

/**
* Created by Taller on 2018/09/11
*/
public interface IExamRecordService {
   /******************* CURD ********************/ 
   // 创建 
   ExamRecord createExamRecord(ExamRecord model); 
   // 删除 
   Boolean deleteExamRecord(Object ids,String reviser);
   // 修改 
   ExamRecord updateExamRecord(ExamRecord model); 
   // 查询 
   List<ExamRecord> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}