package com.qihsoft.webdev.core.service;

import java.util.List;

import com.qihsoft.webdev.core.model.MessageUser;
import com.qihsoft.webdev.utils.bean.Where;

/**
* Created by Qihsoft on 2018/12/08
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