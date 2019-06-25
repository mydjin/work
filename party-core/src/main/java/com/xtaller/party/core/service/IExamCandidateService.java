package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ExamCandidate;

/**
* Created by Taller on 2018/09/01
*/
public interface IExamCandidateService {
   /******************* CURD ********************/ 
   // 创建 
   ExamCandidate createExamCandidate(ExamCandidate model); 
   // 删除 
   Boolean deleteExamCandidate(Object ids,String reviser);
   // 修改 
   ExamCandidate updateExamCandidate(ExamCandidate model) throws Exception;
   // 查询 
   List<ExamCandidate> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}