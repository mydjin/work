package com.qihsoft.webdev.core.service;

import java.util.List;

import com.qihsoft.webdev.core.model.Note;
import com.qihsoft.webdev.utils.bean.Where;

/**
* Created by Qihsoft on 2018/08/17
*/
public interface INoteService {
   /******************* CURD ********************/ 
   // 创建 
   Note createNote(Note model);
   // 删除 
   Boolean deleteNote(Object ids,String reviser);
   // 修改 
   Note updateNote(Note model); 
   // 查询 
   List<Note> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w);
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}