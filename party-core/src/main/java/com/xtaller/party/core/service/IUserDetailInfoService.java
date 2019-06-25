package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.UserDetailInfo;

/**
* Created by Taller on 2018/08/10
*/
public interface IUserDetailInfoService {
   /******************* CURD ********************/ 
   // 创建 
   UserDetailInfo createUserDetailInfo(UserDetailInfo model); 
   // 删除 
   Boolean deleteUserDetailInfo(Object ids,String reviser);
   // 修改 
   UserDetailInfo updateUserDetailInfo(UserDetailInfo model); 
   // 查询 
   List<UserDetailInfo> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}