package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/24
*/
@SuppressWarnings("serial")
@TableName(value = "core_duty")
public class CoreDuty extends Model<CoreDuty> {
  private String id = IdKit.getId(this.getClass());
  private String classId;
  private String gradeId;
  private Integer dutyDate;
  private String studentIds;
  private String cardNo;
  private String creator;
  private Integer creatorType;
  private Integer createTime;
  private Integer localCreateTime;
  private String reviser;
  private Integer reviseTime;
  private Integer isDel;

  public String getId() { 
      return id;
  } 
  public void setId(String id) { 
      this.id = id;
  } 
  public String getClassId() { 
      return classId;
  } 
  public void setClassId(String classId) { 
      this.classId = classId;
  } 
  public String getGradeId() { 
      return gradeId;
  } 
  public void setGradeId(String gradeId) { 
      this.gradeId = gradeId;
  } 
  public Integer getDutyDate() { 
      return dutyDate;
  } 
  public void setDutyDate(Integer dutyDate) { 
      this.dutyDate = dutyDate;
  } 
  public String getStudentIds() { 
      return studentIds;
  } 
  public void setStudentIds(String studentIds) { 
      this.studentIds = studentIds;
  } 
  public String getCardNo() { 
      return cardNo;
  } 
  public void setCardNo(String cardNo) { 
      this.cardNo = cardNo;
  } 
  public String getCreator() { 
      return creator;
  } 
  public void setCreator(String creator) { 
      this.creator = creator;
  } 
  public Integer getCreatorType() { 
      return creatorType;
  } 
  public void setCreatorType(Integer creatorType) { 
      this.creatorType = creatorType;
  } 
  public Integer getCreateTime() { 
      return createTime;
  } 
  public void setCreateTime(Integer createTime) { 
      this.createTime = createTime;
  } 
  public Integer getLocalCreateTime() { 
      return localCreateTime;
  } 
  public void setLocalCreateTime(Integer localCreateTime) { 
      this.localCreateTime = localCreateTime;
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
