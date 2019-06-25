package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.DataArticleAccepter;

/**
* Created by Taller on 2017/12/31
*/
public interface IDataArticleAccepterService {
   /******************* CURD ********************/ 
   // 创建 
   DataArticleAccepter createDataArticleAccepter(DataArticleAccepter model);
   // 删除 
   Boolean deleteDataArticleAccepter(Object ids,String reviser);
   // 修改 
   DataArticleAccepter updateDataArticleAccepter(DataArticleAccepter model); 
   // 查询 
   List<DataArticleAccepter> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}