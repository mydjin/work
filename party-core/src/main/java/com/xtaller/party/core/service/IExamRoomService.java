package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ExamRoom;

/**
* Created by Party on 2019/05/28
*/
public interface IExamRoomService {
   /******************* CURD ********************/ 
   // 创建 
   ExamRoom createExamRoom(ExamRoom model); 
   // 删除 
   Boolean deleteExamRoom(Object ids,String reviser);
   // 修改 
   ExamRoom updateExamRoom(ExamRoom model); 
   // 查询 
   List<ExamRoom> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}