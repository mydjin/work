package com.xtaller.party.core.service;

import com.xtaller.party.core.model.SysOperationRecord;
import com.xtaller.party.utils.bean.Where;

import java.util.List;

/**
* Created by Qihsoft on 2018/05/08
*/
public interface ISysOperationRecordService {
   /******************* CURD ********************/ 
   // 创建 
   SysOperationRecord createSysOperationRecord(SysOperationRecord model);
   // 删除 
   Boolean deleteSysOperationRecord(Object ids, String reviser);
   // 修改 
   SysOperationRecord updateSysOperationRecord(SysOperationRecord model); 
   // 查询 
   List<SysOperationRecord> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w);
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}