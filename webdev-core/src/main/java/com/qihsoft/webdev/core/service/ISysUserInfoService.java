package com.qihsoft.webdev.core.service;

import java.util.List;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.core.model.SysUserInfo;

/**
* Created by Qihsoft on 2019/02/02
*/
public interface ISysUserInfoService {
   /******************* CURD ********************/ 
   // 创建 
   SysUserInfo createSysUserInfo(SysUserInfo model);
   // 删除 
   Boolean deleteSysUserInfo(Object ids,String reviser);
   // 修改 
   SysUserInfo updateSysUserInfo(SysUserInfo model); 
   // 查询 
   List<SysUserInfo> findByIds(Object ids);
  List<SysUserInfo> findByNumber(String number);
      // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}