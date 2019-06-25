package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.DataArticleImages;

/**
* Created by Taller on 2017/11/26
*/
public interface IDataArticleImagesService {
   /******************* CURD ********************/ 
   // 创建 
   DataArticleImages createDataArticleImages(DataArticleImages model);
   // 删除 
   Boolean deleteDataArticleImages(Object ids,String reviser);
   // 修改 
   DataArticleImages updateDataArticleImages(DataArticleImages model); 
   // 查询 
   List<DataArticleImages> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/
   void initImage(String articleId);
 
}