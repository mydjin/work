package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.OnlineCourseUser;

/**
* Created by Taller on 2018/11/06
*/
public interface IOnlineCourseUserService {
   /******************* CURD ********************/ 
   // 创建 
   OnlineCourseUser createOnlineCourseUser(OnlineCourseUser model); 
   // 删除 
   Boolean deleteOnlineCourseUser(Object ids,String reviser);
   // 修改 
   OnlineCourseUser updateOnlineCourseUser(OnlineCourseUser model); 
   // 查询 
   List<OnlineCourseUser> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}