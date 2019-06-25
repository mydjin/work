package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Notice;

/**
* Created by Taller on 2018/08/15
*/
public interface INoticeService {
   /******************* CURD ********************/ 
   // 创建 
   Notice createNotice(Notice model); 
   // 删除 
   Boolean deleteNotice(Object ids,String reviser);
   // 修改 
   Notice updateNotice(Notice model); 
   // 查询 
   List<Notice> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}