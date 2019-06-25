package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.OnlineCourse;

/**
* Created by Taller on 2018/08/23
*/
public interface IOnlineCourseService {
   /******************* CURD ********************/ 
   // 创建 
   OnlineCourse createOnlineCourse(OnlineCourse model); 
   // 删除 
   Boolean deleteOnlineCourse(Object ids,String reviser);
   // 修改 
   OnlineCourse updateOnlineCourse(OnlineCourse model); 
   // 查询 
   List<OnlineCourse> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}