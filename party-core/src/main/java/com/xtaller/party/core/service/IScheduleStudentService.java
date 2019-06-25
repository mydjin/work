package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ScheduleStudent;

/**
* Created by Taller on 2018/08/14
*/
public interface IScheduleStudentService {
   /******************* CURD ********************/ 
   // 创建 
   ScheduleStudent createScheduleStudent(ScheduleStudent model); 
   // 删除 
   Boolean deleteScheduleStudent(Object ids,String reviser);
   // 修改 
   ScheduleStudent updateScheduleStudent(ScheduleStudent model); 
   // 查询 
   List<ScheduleStudent> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}