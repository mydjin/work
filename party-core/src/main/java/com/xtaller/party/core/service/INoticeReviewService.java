package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.NoticeReview;

/**
* Created by Taller on 2018/08/23
*/
public interface INoticeReviewService {
   /******************* CURD ********************/ 
   // 创建 
   NoticeReview createNoticeReview(NoticeReview model); 
   // 删除 
   Boolean deleteNoticeReview(Object ids,String reviser);
   // 修改 
   NoticeReview updateNoticeReview(NoticeReview model); 
   // 查询 
   List<NoticeReview> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}