package com.qihsoft.webdev.core.service;

import java.util.List;

import com.qihsoft.webdev.core.model.Approval;
import com.qihsoft.webdev.utils.bean.Where;

/**
* Created by Qihsoft on 2018/08/14
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