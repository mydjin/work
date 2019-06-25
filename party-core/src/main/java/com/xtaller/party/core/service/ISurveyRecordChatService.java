package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SurveyRecordChat;

/**
* Created by Taller on 2018/08/15
*/
public interface ISurveyRecordChatService {
   /******************* CURD ********************/ 
   // 创建 
   SurveyRecordChat createSurveyRecordChat(SurveyRecordChat model); 
   // 删除 
   Boolean deleteSurveyRecordChat(Object ids,String reviser);
   // 修改 
   SurveyRecordChat updateSurveyRecordChat(SurveyRecordChat model); 
   // 查询 
   List<SurveyRecordChat> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}