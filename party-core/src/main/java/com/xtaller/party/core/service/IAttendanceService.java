package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Attendance;

/**
* Created by Taller on 2018/08/13
*/
public interface IAttendanceService {
   /******************* CURD ********************/ 
   // 创建 
   Attendance createAttendance(Attendance model); 
   // 删除 
   Boolean deleteAttendance(Object ids,String reviser);
   // 修改 
   Attendance updateAttendance(Attendance model); 
   // 查询 
   List<Attendance> findByIds(Object ids);

   List<Attendance> findByScheduleId(Object scheduleId);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}