package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.ISysMenuAuthService;
import com.qihsoft.webdev.core.mapper.SysMenuAuthMapper;
import com.qihsoft.webdev.core.model.SysMenuAuth;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.W;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Created by Qihsoft on 2017/09/18
*/
@Service
public class SysMenuAuthService extends TServiceImpl<SysMenuAuthMapper, SysMenuAuth> implements ISysMenuAuthService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public SysMenuAuth createMenuAuth(SysMenuAuth model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteMenuAuth(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public SysMenuAuth updateMenuAuth(SysMenuAuth model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<SysMenuAuth> findByIds(Object ids) {
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
   //查询权限树
   @Override
   public List<JSONObject> queryList(String where) {
       List<JSONObject> list = baseMapper.queryList(where);
       return F.f2l(list,"id","parentId");
   }

    //查询权限信息
    @Override
    public List<JSONObject> queryAuths(String roleId) {
        List<JSONObject> list = baseMapper.queryAuths(roleId);
        return F.f2l(list,"id","menuId");
    }

}