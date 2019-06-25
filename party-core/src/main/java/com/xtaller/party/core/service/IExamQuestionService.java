package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ExamQuestion;

/**
* Created by Taller on 2018/08/29
*/
public interface IExamQuestionService {
   /******************* CURD ********************/ 
   // 创建 
   ExamQuestion createExamQuestion(ExamQuestion model); 
   // 删除 
   Boolean deleteExamQuestion(Object ids,String reviser);
   // 修改 
   ExamQuestion updateExamQuestion(ExamQuestion model); 
   // 查询 
   List<ExamQuestion> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}