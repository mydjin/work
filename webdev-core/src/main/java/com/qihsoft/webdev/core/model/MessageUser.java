package com.qihsoft.webdev.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qihsoft.webdev.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Qihsoft on 2018/12/08
 */
@SuppressWarnings("serial")
@TableName(value = "message_user")
public class MessageUser extends Model<MessageUser> {
  private String id = IdKit.getId(this.getClass());
  private String messageId;
  private String number;
  private Integer readStatus;
  private Integer readTime;
  private String creator;
  private Integer createTime;
  private String reviser;
  private Integer reviseTime;
  private Integer isDel;

  public String getId() { 
      return id;
  } 
  public void setId(String id) { 
      this.id = id;
  } 
  public String getMessageId() { 
      return messageId;
  } 
  public void setMessageId(String messageId) { 
      this.messageId = messageId;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  } 
  public Integer getReadStatus() { 
      return readStatus;
  } 
  public void setReadStatus(Integer readStatus) { 
      this.readStatus = readStatus;
  } 
  public Integer getReadTime() { 
      return readTime;
  } 
  public void setReadTime(Integer readTime) { 
      this.readTime = readTime;
  } 
  public String getCreator() { 
      return creator;
  } 
  public void setCreator(String creator) { 
      this.creator = creator;
  } 
  public Integer getCreateTime() { 
      return createTime;
  } 
  public void setCreateTime(Integer createTime) { 
      this.createTime = createTime;
  } 
  public String getReviser() { 
      return reviser;
  } 
  public void setReviser(String reviser) { 
      this.reviser = reviser;
  } 
  public Integer getReviseTime() { 
      return reviseTime;
  } 
  public void setReviseTime(Integer reviseTime) { 
      this.reviseTime = reviseTime;
  } 
  public Integer getIsDel() { 
      return isDel;
  } 
  public void setIsDel(Integer isDel) { 
      this.isDel = isDel;
  } 
  @Override
  protected Serializable pkVal() {
      return id;
  }
}
