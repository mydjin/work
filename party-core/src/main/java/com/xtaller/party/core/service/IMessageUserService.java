package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.MessageUser;

/**
* Created by Taller on 2018/12/08
*/
public interface IMessageUserService {
   /******************* CURD ********************/ 
   // 创建 
   MessageUser createMessageUser(MessageUser model); 
   // 删除 
   Boolean deleteMessageUser(Object ids,String reviser);
   // 修改 
   MessageUser updateMessageUser(MessageUser model); 
   // 查询 
   List<MessageUser> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}