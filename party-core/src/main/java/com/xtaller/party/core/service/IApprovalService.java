package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Approval;

/**
* Created by Taller on 2018/08/14
*/
public interface IApprovalService {
   /******************* CURD ********************/ 
   // 创建 
   Approval createApproval(Approval model); 
   // 删除 
   Boolean deleteApproval(Object ids,String reviser);
   // 修改 
   Approval updateApproval(Approval model); 
   // 查询 
   List<Approval> findByIds(Object ids);

   List<Approval> findByApprovalId(Object approvalId);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}