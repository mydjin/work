package com.qihsoft.webdev.core.service;

import java.util.List;

import com.qihsoft.webdev.core.model.Message;
import com.qihsoft.webdev.utils.bean.Where;

/**
* Created by Qihsoft on 2018/12/08
*/
public interface IMessageService {
   /******************* CURD ********************/ 
   // 创建 
   Message createMessage(Message model);
   // 删除 
   Boolean deleteMessage(Object ids,String reviser);
   // 修改 
   Message updateMessage(Message model); 
   // 查询 
   List<Message> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w);
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}