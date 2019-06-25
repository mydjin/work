package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.ISysUserInfoService;
import com.qihsoft.webdev.core.mapper.SysUserInfoMapper;
import com.qihsoft.webdev.core.model.SysUserInfo;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.utils.bean.Where;
/**
 * Created by Qihsoft on 2019/02/02
 */
@Service
public class SysUserInfoService extends TServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public SysUserInfo createSysUserInfo(SysUserInfo model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteSysUserInfo(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public SysUserInfo updateSysUserInfo(SysUserInfo model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<SysUserInfo> findByIds(Object ids) {
       return this.selectByIds(ids);
   }

    @Override
    public List<SysUserInfo> findByNumber(String number) {
        where = W.f(
                W.and("number","eq",number)
        );
        List<SysUserInfo> userInfoList = this.query(where);
        if(userInfoList == null || userInfoList.size() == 0)
            return null;
        return userInfoList;
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
    //分页查
    public Page page(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.getPageCount(w);
        int totalCount = row.getInteger("total");
        if(totalCount == 0)
            return new Page(new ArrayList<JSONObject>(),pageSize,0,0,1);
        // 分页数据
        index = index < 0 ? 1:index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.getPage(w,limit,pageSize);

            return new Page(F.f2l(grades,"id","classCode","majorCode","academyCode"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list,"id","classCode","majorCode","academyCode","creator","reverse");
    }

}