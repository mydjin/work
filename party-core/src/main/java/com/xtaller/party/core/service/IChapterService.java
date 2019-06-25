package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Chapter;

/**
* Created by Taller on 2018/08/28
*/
public interface IChapterService {
   /******************* CURD ********************/ 
   // 创建 
   Chapter createChapter(Chapter model); 
   // 删除 
   Boolean deleteChapter(Object ids,String reviser);
   // 修改 
   Chapter updateChapter(Chapter model); 
   // 查询 
   List<Chapter> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}