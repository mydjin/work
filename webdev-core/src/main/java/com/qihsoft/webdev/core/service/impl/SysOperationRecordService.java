package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.ISysOperationRecordService;
import com.qihsoft.webdev.core.model.SysOperationRecord;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.core.mapper.SysOperationRecordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Qihsoft on 2018/05/08
*/
@Service
public class SysOperationRecordService extends TServiceImpl<SysOperationRecordMapper, SysOperationRecord> implements ISysOperationRecordService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public SysOperationRecord createSysOperationRecord(SysOperationRecord model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteSysOperationRecord(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public SysOperationRecord updateSysOperationRecord(SysOperationRecord model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<SysOperationRecord> findByIds(Object ids) {
       return this.selectByIds(ids);
   }
 
   // 属于createVarietiesList
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

       List<JSONObject> courses = baseMapper.getPage(w,limit,pageSize);
       return new Page(F.f2l(courses,"id","isDefault"), pageSize, totalCount, totalPage, currentPage);
   }

    //全查
    public  List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list,"id","creator","reverse");
    }

    //微信端获取最近三条登陆日志
    public  List<JSONObject> queryWechatLastLoginLogByNumber(String number) {
        List<JSONObject> list = baseMapper.queryWechatLastLoginLogByNumber(number);
        return F.f2l(list,"id","createTime");
    }
}