package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Question;

/**
* Created by Taller on 2018/08/23
*/
public interface IQuestionService {
   /******************* CURD ********************/ 
   // 创建 
   Question createQuestion(Question model); 
   // 删除 
   Boolean deleteQuestion(Object ids,String reviser);
   // 修改 
   Question updateQuestion(Question model); 
   // 查询 
   List<Question> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}