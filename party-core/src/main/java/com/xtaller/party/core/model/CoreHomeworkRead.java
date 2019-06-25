package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/03
*/
@SuppressWarnings("serial")
@TableName(value = "core_homework_read")
public class CoreHomeworkRead extends Model<CoreHomeworkRead> {
  private String id = IdKit.getId(this.getClass());
  private String homeworkId;
  private String userId;
  private Integer readDate;
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
  public String getHomeworkId() { 
      return homeworkId;
  } 
  public void setHomeworkId(String homeworkId) { 
      this.homeworkId = homeworkId;
  } 
  public String getUserId() { 
      return userId;
  } 
  public void setUserId(String userId) { 
      this.userId = userId;
  } 
  public Integer getReadDate() { 
      return readDate;
  } 
  public void setReadDate(Integer readDate) { 
      this.readDate = readDate;
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
