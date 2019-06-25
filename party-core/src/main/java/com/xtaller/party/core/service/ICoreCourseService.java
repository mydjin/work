package com.xtaller.party.core.service;

import java.util.List;

import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.CoreCourse;

/**
* Created by Taller on 2018/01/10
*/
public interface ICoreCourseService {
   /******************* CURD ********************/ 
   // 创建 
   CoreCourse createCoreCourse(CoreCourse model); 
   // 删除 
   Boolean deleteCoreCourse(Object ids,String reviser);
   // 修改 
   CoreCourse updateCoreCourse(CoreCourse model); 
   // 查询 
   List<CoreCourse> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id);
   public Page page(int index, int pageSize, String w);
   /******************* CURD ********************/ 
 
}