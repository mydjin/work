package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/02
*/
@SuppressWarnings("serial")
@TableName(value = "core_party")
public class CoreVisitor extends Model<CoreVisitor> {
  private String id = IdKit.getId(this.getClass());
  private String userId;
  private String name;
  private Integer sex;
  private Integer idCardType;
  private String idCard;
  private String address;
  private String idCardImage;
  private String mobile;
  private Integer applyBeginDate;
  private Integer applyDuration;
  private Integer applyNum;
  private String applyRemark;
  private Integer isVisited;
  private Integer beginDate;
  private Integer endDate;
  private String curImage;
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
  public Integer getSex() { 
      return sex;
  } 
  public void setSex(Integer sex) { 
      this.sex = sex;
  } 
  public Integer getIdCardType() { 
      return idCardType;
  } 
  public void setIdCardType(Integer idCardType) { 
      this.idCardType = idCardType;
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
  public String getIdCardImage() { 
      return idCardImage;
  } 
  public void setIdCardImage(String idCardImage) { 
      this.idCardImage = idCardImage;
  } 
  public String getMobile() { 
      return mobile;
  } 
  public void setMobile(String mobile) { 
      this.mobile = mobile;
  } 
  public Integer getApplyBeginDate() { 
      return applyBeginDate;
  } 
  public void setApplyBeginDate(Integer applyBeginDate) { 
      this.applyBeginDate = applyBeginDate;
  } 
  public Integer getApplyDuration() { 
      return applyDuration;
  } 
  public void setApplyDuration(Integer applyDuration) { 
      this.applyDuration = applyDuration;
  } 
  public Integer getApplyNum() { 
      return applyNum;
  } 
  public void setApplyNum(Integer applyNum) { 
      this.applyNum = applyNum;
  } 
  public String getApplyRemark() { 
      return applyRemark;
  } 
  public void setApplyRemark(String applyRemark) { 
      this.applyRemark = applyRemark;
  } 
  public Integer getIsVisited() { 
      return isVisited;
  } 
  public void setIsVisited(Integer isVisited) { 
      this.isVisited = isVisited;
  } 
  public Integer getBeginDate() { 
      return beginDate;
  } 
  public void setBeginDate(Integer beginDate) { 
      this.beginDate = beginDate;
  } 
  public Integer getEndDate() { 
      return endDate;
  } 
  public void setEndDate(Integer endDate) { 
      this.endDate = endDate;
  } 
  public String getCurImage() { 
      return curImage;
  } 
  public void setCurImage(String curImage) { 
      this.curImage = curImage;
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
