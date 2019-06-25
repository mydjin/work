package com.xtaller.party.core.service;

import java.util.List;

import com.xtaller.party.core.model.CoreVisitor;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;

/**
* Created by Taller on 2018/01/02
*/
public interface ICoreVisitorService {
   /******************* CURD ********************/ 
   // 创建 
   CoreVisitor createCoreVisitor(CoreVisitor model);
   // 删除 
   Boolean deleteCoreVisitor(Object ids,String reviser);
   // 修改 
   CoreVisitor updateCoreVisitor(CoreVisitor model); 
   // 查询 
   List<CoreVisitor> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/
   // 分页
   Page page(int index, int size, String where);
}