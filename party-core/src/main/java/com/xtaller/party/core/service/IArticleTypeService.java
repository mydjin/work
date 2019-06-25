package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ArticleType;

/**
* Created by Taller on 2018/12/08
*/
public interface IArticleTypeService {
   /******************* CURD ********************/ 
   // 创建 
   ArticleType createArticleType(ArticleType model); 
   // 删除 
   Boolean deleteArticleType(Object ids,String reviser);
   // 修改 
   ArticleType updateArticleType(ArticleType model); 
   // 查询 
   List<ArticleType> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}