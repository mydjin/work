package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Video;

/**
* Created by Taller on 2018/08/29
*/
public interface IVideoService {
   /******************* CURD ********************/ 
   // 创建 
   Video createVideo(Video model); 
   // 删除 
   Boolean deleteVideo(Object ids,String reviser);
   // 修改 
   Video updateVideo(Video model); 
   // 查询 
   List<Video> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}