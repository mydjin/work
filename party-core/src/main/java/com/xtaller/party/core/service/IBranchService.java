package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Branch;

/**
* Created by Taller on 2018/11/30
*/
public interface IBranchService {
   /******************* CURD ********************/ 
   // 创建 
   Branch createBranch(Branch model); 
   // 删除 
   Boolean deleteBranch(Object ids,String reviser);
   // 修改 
   Branch updateBranch(Branch model); 
   // 查询 
   List<Branch> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}