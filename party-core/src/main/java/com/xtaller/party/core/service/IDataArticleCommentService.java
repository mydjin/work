package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.DataArticleComment;

/**
* Created by Taller on 2017/11/24
*/
public interface IDataArticleCommentService {
   /******************* CURD ********************/ 
   // 创建 
   DataArticleComment createDataArticleComment(DataArticleComment model);
   // 删除 
   Boolean deleteDataArticleComment(Object ids,String reviser);
   // 修改 
   DataArticleComment updateDataArticleComment(DataArticleComment model); 
   // 查询 
   List<DataArticleComment> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}