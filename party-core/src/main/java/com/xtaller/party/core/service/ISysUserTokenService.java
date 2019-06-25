package com.xtaller.party.core.service;

import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.SysUserToken;

import java.util.List;

/**
* Created by Taller on 2017/10/03
*/
public interface ISysUserTokenService {
   /******************* CURD ********************/ 
   // 创建
   SysUserToken createUserToken(SysUserToken model);
   // 删除
   Boolean deleteUserToken(Object ids, String reviser);
   // 修改
   SysUserToken updateUserToken(SysUserToken model);
   // 查询
   List<SysUserToken> findByIds(Object ids);
   // 属于
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在
   Boolean existId(Object id); 
   /******************* CURD ********************/ 


}