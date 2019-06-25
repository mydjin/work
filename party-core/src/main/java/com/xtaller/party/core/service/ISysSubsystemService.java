package com.xtaller.party.core.service;

import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysSubsystem;

import java.util.List;

/**
* Created by Taller on 2017/09/18
*/
public interface ISysSubsystemService {
   /******************* CURD ********************/ 
   // 创建
   SysSubsystem createSubsystem(SysSubsystem model);
   // 删除
   Boolean deleteSubsystem(Object ids, String reviser);
   // 修改
   SysSubsystem updateSubsystem(SysSubsystem model);
   // 查询
   List<SysSubsystem> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/
   //读取
   List<SysSubsystem> get();
 
}