package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.LinkUser;

/**
* Created by Taller on 2018/10/08
*/
public interface ILinkUserService {
   /******************* CURD ********************/ 
   // 创建 
   LinkUser createLinkUser(LinkUser model); 
   // 删除 
   Boolean deleteLinkUser(Object ids,String reviser);
   // 修改 
   LinkUser updateLinkUser(LinkUser model); 
   // 查询 
   List<LinkUser> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}