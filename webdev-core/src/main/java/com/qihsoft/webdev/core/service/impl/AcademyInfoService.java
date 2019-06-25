package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.service.IAcademyInfoService;
import com.qihsoft.webdev.core.model.AcademyInfo;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.core.mapper.AcademyInfoMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Created by Qihsoft on 2018/06/12
*/
@Service
public class AcademyInfoService extends TServiceImpl<AcademyInfoMapper, AcademyInfo> implements IAcademyInfoService {
   /**************************CURD begin******************************/ 
   // 创建
   @Override
   public AcademyInfo createAcademyInfo(AcademyInfo model) {
       if(this.insert(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 删除
   @Override
   public Boolean deleteAcademyInfo(Object ids,String reviser) {
       return this.delete(ids,reviser);
   }
 
   // 修改
   @Override
   public AcademyInfo updateAcademyInfo(AcademyInfo model) {
       if(this.update(model))
           return this.selectById(model.getId());
       return null;
   }
 
   // 查询
   @Override
   public List<AcademyInfo> findByIds(Object ids) {
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
   @Override
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

       List<JSONObject> academy_info = baseMapper.getPage(w,limit,pageSize);

       return new Page(F.f2l(academy_info,"id"), pageSize, totalCount, totalPage, currentPage);
   }

    @Override
    public List<JSONObject> getCampusByCode( String w) {
        List<JSONObject> list = baseMapper.getCampusByCode(w);
        return F.f2l(list,"campus");
    }

    //全查
    public List<JSONObject> queryCodeAndName(String where) {
        List<JSONObject> list = baseMapper.queryCodeAndName(where);
        return F.f2l(list,"id","creator","reverse");
    }


    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list,"id","creator","reverse");
    }

    //根据组织机构获取用户列表
    public  List<JSONObject> queryUserByRange(JSONArray rangeArray,Integer type) {
        List<JSONObject> number=new ArrayList<>();

        for (int i=0;i<rangeArray.size();i++){
            JSONObject range =rangeArray.getJSONObject(i);
            String title =range.getString("code");

            String wKey = "";
            String tKey = "";
            if (!V.isEmpty(type)){
                if(type!=0){
                    tKey = S.apppend("and type = '", type, "' " );
                }
            }

            if(range.getString("type").equals("academy")){
                if (!V.isEmpty(title))
                    //FIXME 修改id为需要查询的字段
                    wKey = S.apppend(" and academyCode = '", title, "' " );
                List<JSONObject> list = baseMapper.queryUserDepart(tKey+wKey);
                for (JSONObject obj : list) {
                    number.add(obj);
                }
            }else   if(range.getString("type").equals("class")){
                if (!V.isEmpty(title))
                    //FIXME 修改id为需要查询的字段
                    wKey = S.apppend(" and classCode = '", title, "' ");
                List<JSONObject> list = baseMapper.queryUserDepart(tKey+wKey);
                for (JSONObject obj : list) {
                    number.add(obj);
                }
            }
        }
        Set set = new HashSet();
        List<JSONObject> listNew=new ArrayList<>();
        set.addAll(number);
        listNew.addAll(set);

        return F.f2l(listNew,"number");
    }

    //根据组织机构判断用户是否有权限
    public  boolean checkUserByRange(JSONArray rangeArray,JSONObject user) {
        boolean flag =false;
        for (int i=0;i<rangeArray.size();i++){
            JSONObject range =rangeArray.getJSONObject(i);
            String title =range.getString("code");
            if(range.getString("type").equals("academy")){
               if(title.equals(user.getString("academyCode"))){
                flag =true;
               }
            }else  if(range.getString("type").equals("class")){
                if(title.equals(user.getString("class"))){
                    flag =true;
                }
            }
        }

        return flag;
    }


}