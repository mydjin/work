package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/09
 */
@SuppressWarnings("serial")
@TableName(value = "note")
public class Note extends Model<Note> {
  private String id = IdKit.getId(this.getClass());
  private String theme;
  private String detail;
  private String number;
  private String name;
  private String phone;
  private Integer type;
  private Integer sendStatus;
  private Integer sendTime;
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
  public String getTheme() { 
      return theme;
  } 
  public void setTheme(String theme) { 
      this.theme = theme;
  } 
  public String getDetail() { 
      return detail;
  } 
  public void setDetail(String detail) { 
      this.detail = detail;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getPhone() { 
      return phone;
  } 
  public void setPhone(String phone) { 
      this.phone = phone;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public Integer getSendStatus() { 
      return sendStatus;
  } 
  public void setSendStatus(Integer sendStatus) { 
      this.sendStatus = sendStatus;
  } 
  public Integer getSendTime() { 
      return sendTime;
  } 
  public void setSendTime(Integer sendTime) { 
      this.sendTime = sendTime;
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
