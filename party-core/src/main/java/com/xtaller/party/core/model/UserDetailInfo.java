package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/08/20
 */
@SuppressWarnings("serial")
@TableName(value = "user_detail_info")
public class UserDetailInfo extends Model<UserDetailInfo> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private Integer politicalOutlook;
  private String academyCode;
  private String majorCode;
  private String myClass;
  private Integer studyStatus;
  private String departCode;
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
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
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
  public String getMyClass() { 
      return myClass;
  } 
  public void setMyClass(String myClass) { 
      this.myClass = myClass;
  } 
  public Integer getStudyStatus() { 
      return studyStatus;
  } 
  public void setStudyStatus(Integer studyStatus) { 
      this.studyStatus = studyStatus;
  } 
  public String getDepartCode() { 
      return departCode;
  } 
  public void setDepartCode(String departCode) { 
      this.departCode = departCode;
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
