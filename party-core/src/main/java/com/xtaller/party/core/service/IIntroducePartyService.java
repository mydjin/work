package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.IntroduceParty;

/**
* Created by Taller on 2018/10/08
*/
public interface IIntroducePartyService {
   /******************* CURD ********************/ 
   // 创建 
   IntroduceParty createIntroduceParty(IntroduceParty model); 
   // 删除 
   Boolean deleteIntroduceParty(Object ids,String reviser);
   // 修改 
   IntroduceParty updateIntroduceParty(IntroduceParty model); 
   // 查询 
   List<IntroduceParty> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}