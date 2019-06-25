package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Schedule;

/**
* Created by Taller on 2018/08/13
*/
public interface IScheduleService {
   /******************* CURD ********************/ 
   // 创建 
   Schedule createSchedule(Schedule model); 
   // 删除 
   Boolean deleteSchedule(Object ids,String reviser);
   // 修改 
   Schedule updateSchedule(Schedule model); 
   // 查询 
   List<Schedule> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}