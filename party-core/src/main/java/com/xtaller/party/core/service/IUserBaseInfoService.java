package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.UserBaseInfo;

/**
* Created by Taller on 2018/08/10
*/
public interface IUserBaseInfoService {
   /******************* CURD ********************/ 
   // 创建 
   UserBaseInfo createUserBaseInfo(UserBaseInfo model); 
   // 删除 
   Boolean deleteUserBaseInfo(Object ids,String reviser);
   // 修改 
   UserBaseInfo updateUserBaseInfo(UserBaseInfo model); 
   // 查询 
   List<UserBaseInfo> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}