package com.qihsoft.webdev.core.service;

import com.qihsoft.webdev.core.model.Article;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;

import java.util.List;

/**
* Created by Taller on 2018/06/10
*/
public interface IArticleService {
   /******************* CURD ********************/ 
   // 创建 
   Article createArticle(Article model);
   // 删除 
   Boolean deleteArticle(Object ids, String reviser);
   // 修改 
   Article updateArticle(Article model); 
   // 查询 
   List<Article> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/
   public Page page(int index, int pageSize, String w);
}