package com.qihsoft.webdev.core.service.impl;

import com.qihsoft.webdev.core.service.ISysAutoCodeService;
import com.qihsoft.webdev.core.model.SysAutoCode;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.core.mapper.SysAutoCodeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/12
*/
@Service
public class SysAutoCodeService extends TServiceImpl<SysAutoCodeMapper, SysAutoCode> implements ISysAutoCodeService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public SysAutoCode createAutoCode(SysAutoCode model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteAutoCode(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public SysAutoCode updateAutoCode(SysAutoCode model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<SysAutoCode> findByIds(Object ids) {
       return this.selectByIds(ids);
   }
 
   // 属于
   @Override
   public Boolean exist(List<Where> w) {
       return this.query(w).size() > 0;
   }
 
   /**************************CURD end********************************/ 

}