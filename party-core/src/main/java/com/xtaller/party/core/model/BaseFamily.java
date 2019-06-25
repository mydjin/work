package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2017/11/28
*/
@SuppressWarnings("serial")
@TableName(value = "base_family")
public class BaseFamily extends Model<BaseFamily> {
  private String id = IdKit.getId(this.getClass());
  private String userId;
  private String name;
  private String image;
  private Integer sex;
  private String idCard;
  private String address;
  private String workingPlace;
  private String mobile;
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
  public String getUserId() { 
      return userId;
  } 
  public void setUserId(String userId) { 
      this.userId = userId;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getImage() { 
      return image;
  } 
  public void setImage(String image) { 
      this.image = image;
  } 
  public Integer getSex() { 
      return sex;
  } 
  public void setSex(Integer sex) { 
      this.sex = sex;
  } 
  public String getIdCard() { 
      return idCard;
  } 
  public void setIdCard(String idCard) { 
      this.idCard = idCard;
  } 
  public String getAddress() { 
      return address;
  } 
  public void setAddress(String address) { 
      this.address = address;
  } 
  public String getWorkingPlace() { 
      return workingPlace;
  } 
  public void setWorkingPlace(String workingPlace) { 
      this.workingPlace = workingPlace;
  } 
  public String getMobile() { 
      return mobile;
  } 
  public void setMobile(String mobile) { 
      this.mobile = mobile;
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
