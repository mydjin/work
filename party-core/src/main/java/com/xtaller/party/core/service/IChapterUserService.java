package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.ChapterUser;

/**
* Created by Taller on 2018/11/05
*/
public interface IChapterUserService {
   /******************* CURD ********************/ 
   // 创建 
   ChapterUser createChapterUser(ChapterUser model); 
   // 删除 
   Boolean deleteChapterUser(Object ids,String reviser);
   // 修改 
   ChapterUser updateChapterUser(ChapterUser model); 
   // 查询 
   List<ChapterUser> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}