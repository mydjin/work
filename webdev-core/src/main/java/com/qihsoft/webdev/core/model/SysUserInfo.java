package com.qihsoft.webdev.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qihsoft.webdev.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Qihsoft on 2019/02/03
 */
@SuppressWarnings("serial")
@TableName(value = "sys_user_info")
public class SysUserInfo extends Model<SysUserInfo> {
  private String id = IdKit.getId(this.getClass());
  private String name;
  private String number;
  private String phone;
  private Integer sex;
  private String nationCode;
  private Integer birthday;
  private String originPlace;
  private String idCard;
  private Integer type;
  private Integer politicalOutlook;
  private String academyCode;
  private String majorCode;
  private String classCode;
  private Integer studyStatus;
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
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  } 
  public String getPhone() { 
      return phone;
  } 
  public void setPhone(String phone) { 
      this.phone = phone;
  } 
  public Integer getSex() { 
      return sex;
  } 
  public void setSex(Integer sex) { 
      this.sex = sex;
  } 
  public String getNationCode() { 
      return nationCode;
  } 
  public void setNationCode(String nationCode) { 
      this.nationCode = nationCode;
  } 
  public Integer getBirthday() { 
      return birthday;
  } 
  public void setBirthday(Integer birthday) { 
      this.birthday = birthday;
  } 
  public String getOriginPlace() { 
      return originPlace;
  } 
  public void setOriginPlace(String originPlace) { 
      this.originPlace = originPlace;
  } 
  public String getIdCard() { 
      return idCard;
  } 
  public void setIdCard(String idCard) { 
      this.idCard = idCard;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public Integer getPoliticalOutlook() { 
      return politicalOutlook;
  } 
  public void setPoliticalOutlook(Integer politicalOutlook) { 
      this.politicalOutlook = politicalOutlook;
  } 
  public String getAcademyCode() { 
      return academyCode;
  } 
  public void setAcademyCode(String academyCode) { 
      this.academyCode = academyCode;
  } 
  public String getMajorCode() { 
      return majorCode;
  } 
  public void setMajorCode(String majorCode) { 
      this.majorCode = majorCode;
  } 
  public String getClassCode() { 
      return classCode;
  } 
  public void setClassCode(String classCode) { 
      this.classCode = classCode;
  } 
  public Integer getStudyStatus() { 
      return studyStatus;
  } 
  public void setStudyStatus(Integer studyStatus) { 
      this.studyStatus = studyStatus;
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
