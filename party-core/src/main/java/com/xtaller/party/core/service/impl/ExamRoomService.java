package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ExamRoomMapper;
import com.xtaller.party.core.model.ExamRoom;
import com.xtaller.party.core.service.IExamRoomService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.V;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;
/**
 * Created by Party on 2019/05/28
 */
@Service
public class ExamRoomService extends TServiceImpl<ExamRoomMapper,ExamRoom> implements IExamRoomService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public ExamRoom createExamRoom(ExamRoom model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteExamRoom(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public ExamRoom updateExamRoom(ExamRoom model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<ExamRoom> findByIds(Object ids) {
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

            return new Page(F.f2l(grades,"id","examId","startTime","endTime"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list,"id","creator","reverse","examId","startTime","endTime");
    }

    //全查
    public JSONObject queryInfo(String id) {
        JSONObject obj = baseMapper.queryInfo(id);
        return F.f2j(obj,"id","creator","reverse","examId","startTime","endTime");
    }

    //根据考场Id删除考生
    public Integer deleteStudentByRoomId(String roomId) {
        if(V.isEmpty(roomId)){
         return  null;
        }
        int count = baseMapper.deleteStudentByRoomId(roomId);
        return count;
    }

    //根据考场Id查询全部考生
    public String[] queryStudentsByRoomId(String roomId) {
        String[] list = baseMapper.queryStudentsByRoomId(roomId);
        return list;
    }
}