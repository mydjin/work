package com.qihsoft.webdev.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * Created by qihsoft on 2018/12/08
 */
public interface MessageMapper extends BaseMapper<Message> {


 // fixme:批量插入
    @Insert("INSERT INTO message (id,name,content,topic,number,type,server,sendStatus,sendTime,linkId,creator,createTime)VALUES " +
            "${values} ; ")
   int sendMessages(@Param("values") String values);



   //通用查询
    @Select("SELECT a.* " + 
            ",FROM_UNIXTIME(sendTime) sendTimeStr  " +
              "FROM message a " + 
            "JOIN (SELECT id from message where isDel = 0 and sendStatus =1 ${where} order by createTime desc " +
            ")b ON a.id=b.id order by a.sendTime desc LIMIT #{index}, #{size}")
    List<JSONObject> getPage(@Param("where") String where, 
                             @Param("index") int index, 
                             @Param("size") int size);

    @Select("select count(1) total from message where isDel = 0 and sendStatus =1 ${where} ")
    JSONObject getPageCount(@Param("where") String where);

    @Select("SELECT a.* FROM message a where 1=1 and isDel=0 and sendStatus =1  ${where}  order by sendTime desc")
    List<JSONObject> queryAll(@Param("where") String where);

    @Select("SELECT a.*,c.readStatus,FROM_UNIXTIME(sendTime) sendTimeStr FROM message a  " +
         "join (SELECT id from message where 1=1 and isDel=0 and sendStatus =1 and id = '${id}'  )b on a.id =b.id  " +
         "left join message_user c on c.messageId=a.id and c.number = '${number}' ")
    JSONObject queryById(@Param("id") String id, @Param("number") String number );

    //后台端
    @Select("SELECT * from ( " +
            "SELECT a.*,c.readStatus " +
            ",FROM_UNIXTIME(sendTime) sendTimeStr  " +
            "FROM message a " +
            "JOIN (SELECT id from message where isDel = 0 and sendStatus =1 and number = '${number}'  ${where} " +
            "order by createTime desc  )b ON a.id=b.id " +
            "left join message_user c on c.messageId=a.id and c.number = '${number}') message " +
            "order by readStatus,sendTime desc LIMIT #{index}, #{size} ")
    List<JSONObject> getPage_user(@Param("where") String where,
                             @Param("index") int index,
                             @Param("size") int size,
                                  @Param("number") String number );


    @Select("select count(1) total from message where isDel = 0 and sendStatus =1 and number = '${number}' ${where} ")
    JSONObject getPageCount_user(@Param("where") String where,
                                 @Param("number") String number );


    @Select("SELECT a.id,a.number,a.topic,a.content,a.sendTime,a.linkId,c.readStatus,FROM_UNIXTIME(sendTime) sendTimeStr FROM message a  "+
            "       join (SELECT id from message where 1=1 and isDel=0 and sendStatus =1 ${where}  )b on a.id =b.id  " +
            "      left join message_user c on c.messageId=a.id and c.number = '${number}' ")
    List<JSONObject> queryAll_user(@Param("where") String where,@Param("number") String number);


 //首页

    @Select("SELECT * from (  " +
            "SELECT a.id,a.number,a.topic,a.content,a.sendTime,a.linkId,c.readStatus " +
            ",FROM_UNIXTIME(sendTime) sendTimeStr  " +
            "FROM message a " +
            "JOIN (SELECT id from message where isDel = 0 and sendStatus =1 and number = '${number}' ${where} " +
            "order by createTime desc )b ON a.id=b.id   " +
            "left join message_user c on c.messageId=a.id and c.number = '${number}') message  " +
            "order by readStatus,sendTime desc LIMIT 0, 3 ")
    List<JSONObject> getIndex_user(@Param("where") String where,@Param("number") String number );

    @Select("SELECT count(1) total  from (  " +
            "SELECT a.*,IFNULL(c.readStatus,2) readStatus" +
            ",FROM_UNIXTIME(sendTime) sendTimeStr " +
            "FROM message a " +
            "JOIN (SELECT id from message where isDel = 0 and sendStatus =1 and number = '${number}' ${where} " +
            "order by createTime )b ON a.id=b.id   " +
            "left join message_user c on c.messageId=a.id and c.number = '${number}') message  " +
            "where readStatus =2 ")
    JSONObject getIndex_Count(@Param("where") String where,@Param("number") String number );

    @Select("  select count(1) num,type " +
            "    from message " +
            "    WHERE  sendStatus =1 and number = '${number}' ${where} " +
            "    GROUP BY type " +
            "    ORDER BY sendTime desc ")
    List<JSONObject> getIndex_Type(@Param("where") String where,@Param("number") String number );





 @Select("SELECT a.id,a.topic FROM message a where 1=1 and isDel=0 and sendStatus =1 and number = '${number}' ${where} order by createTime desc")
 List<JSONObject> getIdName(@Param("where") String where,@Param("number") String number);

}