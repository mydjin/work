package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.IMessageService;
import com.qihsoft.webdev.core.model.Message;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.utils.kit.IdKit;
import com.qihsoft.webdev.utils.kit.TimeKit;
import com.qihsoft.webdev.core.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qihsoft on 2018/12/08
 */
@Service
public class MessageService extends TServiceImpl<MessageMapper, Message> implements IMessageService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public Message createMessage(Message model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteMessage(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public Message updateMessage(Message model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<Message> findByIds(Object ids) {
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

            return new Page(F.f2l(grades,"id","linkId"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list,"id","creator","reverse","linkId");
    }


    //分页查
    public Page page_user(int index, int pageSize, String w,String number) {
        // 总记录数
        JSONObject row = baseMapper.getPageCount_user(w,number);
        int totalCount = row.getInteger("total");
        if(totalCount == 0)
            return new Page(new ArrayList<JSONObject>(),pageSize,0,0,1);
        // 分页数据
        index = index < 0 ? 1:index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.getPage_user(w,limit,pageSize,number);

        return new Page(F.f2l(grades,"id","linkId"), pageSize, totalCount, totalPage, currentPage);
    }


    //全查
    public List<JSONObject> queryAll_user(String where,String number) {
        List<JSONObject> list = baseMapper.queryAll_user(where,number);
        return F.f2l(list,"id","creator","reverse","linkId");
    }

    //PC端首页
    public JSONObject getIndex_user(String where,String number) {
        JSONObject object =new JSONObject();
        List<JSONObject> list =  F.f2l(baseMapper.getIndex_user(where,number),"id","linkId");
        List<JSONObject> types =  F.f2l(baseMapper. getIndex_Type(where,number),"id","linkId");

        JSONObject row = baseMapper.getIndex_Count(where,number);
        int totalCount = row.getInteger("total");
        object.put("messages",list);
        object.put("count",totalCount);
        object.put("type",types);

        return  object;
    }

    //PC端首页
    public JSONObject getById(String id,String number) {
        JSONObject object =F.f2j(baseMapper.queryById(id,number),"id","linkId","sendTimeStr");


        return  object;
    }

    //查询名字id
    public List<JSONObject> queryIdName(String where,String number) {
        List<JSONObject> list = baseMapper.getIdName(where,number);
        return F.f2l(list,"id","linkId");
    }

    //批量插入
    public int  sendMessage(List<Message>  sendMessages) {
        String values = "";

        for (int i=0;i<sendMessages.size();i++) {
            Message message = sendMessages.get(i);
            if(V.isEmpty(message.getSendStatus())){
                message.setSendStatus(1);
            }
            if(V.isEmpty( message.getSendTime())){
                message.setSendTime((int) TimeKit.getTimestamp());
            }

            message.setId(IdKit.getId(message.getClass()));//自动生成id
            message.setCreateTime((int) TimeKit.getTimestamp());
            String value = "";
            value += "('" + message.getId() + "',";
            value += "'" + message.getName() + "',";
            value += "'" + message.getContent() + "',";
            value += "'" + message.getTopic() + "',";
            value += "'" + message.getNumber() + "',";
            value += "'" + message.getType() + "',";
            value += "'" + message.getServer() + "',";
            value += "'" + message.getSendStatus()+ "',";
            value += "'" + message.getSendTime() + "',";
            if(message.getLinkId()==null) {
                value += "NULL,";
            }else {
                value += "'" + message.getLinkId() + "',";
            }
            value += "'" + message.getCreator() + "',";
            value += "'" + message.getCreateTime() + "')";

            if(i+1!=sendMessages.size()){
                value +=",";
            }

            values+=value;
        }
        int count  =0;
        if(sendMessages.size()!=0){
            count  = baseMapper.sendMessages(values);
        }


        return count;
    }

}