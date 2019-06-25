package com.xtaller.party.core.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.DataArticle;

/**
* Created by Taller on 2017/11/24
*/
public interface IDataArticleService {
   /******************* CURD ********************/ 
   // 创建 
   JSONObject createDataArticle(DataArticle model, String images);
   // 创建
   JSONObject createDataArticle(DataArticle model, String gradIds,String classIds,String studentIds,String familyIds,String teacherIds) ;
   // 删除 
   Boolean deleteDataArticle(Object ids,String reviser);
   // 修改 
   JSONObject updateDataArticle(DataArticle model, String images);
   // 查询 
   List<DataArticle> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/

   // 分页
   Page page(int index, int size, String where);

   JSONObject getById(String id);

   JSONObject setArticleTop(DataArticle model);


}