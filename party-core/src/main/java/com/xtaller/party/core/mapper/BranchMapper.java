package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Branch;

import java.util.List;
/**
 * Created by party on 2018/11/30
 */
public interface BranchMapper extends BaseMapper<Branch> {

    @Select("SELECT a.*,u.name academyName " +
              "FROM branch a " + 
            "JOIN (SELECT id from branch where isDel = 0 ${where} " + 
            "order by createTime asc LIMIT #{index}, #{size})b ON a.id=b.id LEFT JOIN academy_info u on u.code =  a.academyCode order by a.createTime asc  ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size); 

    @Select("select count(1) total from branch where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where);

    @Select( "SELECT a.*,u.name FROM branch a " +
        "JOIN (SELECT id from branch where isDel = 0 ${where} )b ON a.id=b.id" +
            " LEFT JOIN academy_info u on u.code =  a.academyCode  order by createTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    //通过学院代码找学院所有的支部

    @Select( "SELECT a.*,u.name FROM branch a " +
            "JOIN (SELECT id from branch where isDel = 0 and academyCode = '${code}' )b ON a.id=b.id" +
            " LEFT JOIN academy_info u on u.code =  a.academyCode  order by createTime desc")
    List<JSONObject> queryByAcademy(@Param("code") String code);



}