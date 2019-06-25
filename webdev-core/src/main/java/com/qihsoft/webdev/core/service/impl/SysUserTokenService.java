package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.ISysUserTokenService;
import com.qihsoft.webdev.core.model.SysUserToken;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.J;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.utils.kit.IdKit;
import com.qihsoft.webdev.utils.kit.TimeKit;
import com.qihsoft.webdev.core.mapper.SysUserTokenMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Created by Qihsoft on 2017/10/03
*/
@Service
public class SysUserTokenService extends TServiceImpl<SysUserTokenMapper, SysUserToken> implements ISysUserTokenService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public SysUserToken createUserToken(SysUserToken model) {
       //判断是否已经存在数据
       where = W.f(
               W.and("userId","eq",model.getUserId()),
               W.and("server","eq",model.getServer()),
               W.and("type","eq",model.getType())


       );
       List<SysUserToken> uts = this.query(where);
       if(uts.size() > 0){
           SysUserToken ut = uts.get(0);
           ut.setToken(model.getToken());
           ut.setType(model.getType());
           ut.setIsDel(0);

           this.update(ut);
       }else {
           JSONObject ut = new JSONObject();
           ut.put("id", IdKit.getId(SysUserToken.class));
           ut.put("reviseTime", TimeKit.getTimestamp());
           ut.put("userId",model.getUserId());
           ut.put("server",model.getServer());
           ut.put("type",model.getType());
           ut.put("token",model.getToken());
           ut.put("isdel",0);
           this.insert(J.o2m(ut,SysUserToken.class));
       }
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteUserToken(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public SysUserToken updateUserToken(SysUserToken model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<SysUserToken> findByIds(Object ids) {
       return this.selectByIds(ids);
   }
 
   // 属于
   @Override
   public Boolean exist(List<Where> w) {
       w.add(new Where("1"));
       return this.query(w).size() > 0;
   }
 
   // 查询一个id是否存在
   @Override
   public Boolean existId(Object id) {
       where = W.f(
               W.and("id","eq",id),
               W.field("1")
       );
       return this.query(where).size() > 0;
   }
 
   /**************************CURD end********************************/ 


}