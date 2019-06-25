package com.xtaller.party.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.xtaller.party.core.model.Approval;

import java.util.List;
/**
 * Created by party on 2018/08/14
 */
public interface ApprovalMapper extends BaseMapper<Approval> {

    @Select(" SELECT a.*,c.name,FROM_UNIXTIME(a.createTime) createTimeStr FROM approval a " +
            " JOIN (SELECT id from approval where isDel = 0 ${where} order by createTime desc " +
            " LIMIT #{index}, #{size})b ON a.id=b.id " +
            " left join user_base_info c ON a.number=c.number " +
            " order by a.createTime desc ")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size);

    @Select("select count(1) total from approval where isDel = 0 ${where} ") 
    JSONObject getPageCount(@Param("where") String where); 

    @Select("SELECT a.* FROM approval a where 1=1 and isDel=0 ${where}  order by createTime desc ")
    List<JSONObject> queryAll(@Param("where") String where);


    /*改用视图，获取所有再审核的表的信息
SELECT a.id,a.approvalId,a.approvalType,a.stage,a.number,c.name,a.createTime,
FROM_UNIXTIME(a.createTime) createTimeStr FROM approval a
JOIN (SELECT id from approval where isDel = 0 and status =1 order by createTime desc )b ON a.id=b.id
left join user_base_info c ON a.number=c.number
UNION
SELECT a.id,a.id approvalId,case when a.type = 1 then 6 when a.type = 2 then 7 else 0 end type,
0 stage,a.number,c.name,a.createTime,FROM_UNIXTIME(a.createTime) createTimeStr
FROM vacate a join (select id from vacate where isDel =0 and status =1 )b on b.id=a.id
left join user_base_info c ON a.number=c.number

ORDER BY createTime desc
视图字段a.id,a.approvalId,a.approvalType,a.stage,a.number,c.name,a.createTime,FROM_UNIXTIME(a.createTime) createTimeStr
*/
    @Select(" SELECT * from v_todo_list where 1=1 ${where} LIMIT #{index}, #{size}")
    List<JSONObject> getTodoPage(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size);
    @Select("select count(1) total from v_todo_list where 1=1 ${where} ")
    JSONObject getTodoPageCount(@Param("where") String where);



}