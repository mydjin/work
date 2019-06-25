package com.qihsoft.webdev.core.service;

import com.qihsoft.webdev.core.model.Picture;
import com.qihsoft.webdev.utils.bean.Where;

import java.util.List;

/**
* Created by Taller on 2018/08/23
*/
public interface IPictureService {
   /******************* CURD ********************/ 
   // 创建 
   Picture createPicture(Picture model);
   // 删除 
   Boolean deletePicture(Object ids, String reviser);
   // 修改 
   Picture updatePicture(Picture model); 
   // 查询 
   List<Picture> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}