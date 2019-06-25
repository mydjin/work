package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Message;

/**
* Created by Taller on 2018/12/08
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