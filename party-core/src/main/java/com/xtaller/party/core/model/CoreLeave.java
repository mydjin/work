package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2017/12/21
*/
@SuppressWarnings("serial")
@TableName(value = "core_leave")
public class CoreLeave extends Model<CoreLeave> {
  private String id = IdKit.getId(this.getClass());
  private String classId;
  private String studentId;
  private Integer beginTime;
  private Integer endTime;
  private String day;
  private Integer remark;
  private String teacherId;
  private String memo;
  private Integer status;
  private String opinion;
  private Integer checkTime;
  private Integer isNotice;
  private String familyId1;
  private String confirm1;
  private Integer confirmTime1;
  private String familyId2;
  private String confirm2;
  private Integer confirmTime2;
  private String creator;
  private Integer createTime;
  private String reviser;
  private Integer reviseTime;
  private Integer isSms;
  private String smsId;
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
  public String getStudentId() { 
      return studentId;
  } 
  public void setStudentId(String studentId) { 
      this.studentId = studentId;
  } 
  public Integer getBeginTime() { 
      return beginTime;
  } 
  public void setBeginTime(Integer beginTime) { 
      this.beginTime = beginTime;
  } 
  public Integer getEndTime() { 
      return endTime;
  } 
  public void setEndTime(Integer endTime) { 
      this.endTime = endTime;
  } 
  public String getDay() { 
      return day;
  } 
  public void setDay(String day) { 
      this.day = day;
  } 
  public Integer getRemark() { 
      return remark;
  } 
  public void setRemark(Integer remark) { 
      this.remark = remark;
  } 
  public String getTeacherId() { 
      return teacherId;
  } 
  public void setTeacherId(String teacherId) { 
      this.teacherId = teacherId;
  } 
  public String getMemo() { 
      return memo;
  } 
  public void setMemo(String memo) { 
      this.memo = memo;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public String getOpinion() { 
      return opinion;
  } 
  public void setOpinion(String opinion) { 
      this.opinion = opinion;
  } 
  public Integer getCheckTime() { 
      return checkTime;
  } 
  public void setCheckTime(Integer checkTime) { 
      this.checkTime = checkTime;
  } 
  public Integer getIsNotice() { 
      return isNotice;
  } 
  public void setIsNotice(Integer isNotice) { 
      this.isNotice = isNotice;
  } 
  public String getFamilyId1() { 
      return familyId1;
  } 
  public void setFamilyId1(String familyId1) { 
      this.familyId1 = familyId1;
  } 
  public String getConfirm1() { 
      return confirm1;
  } 
  public void setConfirm1(String confirm1) { 
      this.confirm1 = confirm1;
  } 
  public Integer getConfirmTime1() { 
      return confirmTime1;
  } 
  public void setConfirmTime1(Integer confirmTime1) { 
      this.confirmTime1 = confirmTime1;
  } 
  public String getFamilyId2() { 
      return familyId2;
  } 
  public void setFamilyId2(String familyId2) { 
      this.familyId2 = familyId2;
  } 
  public String getConfirm2() { 
      return confirm2;
  } 
  public void setConfirm2(String confirm2) { 
      this.confirm2 = confirm2;
  } 
  public Integer getConfirmTime2() { 
      return confirmTime2;
  } 
  public void setConfirmTime2(Integer confirmTime2) { 
      this.confirmTime2 = confirmTime2;
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
  public Integer getIsSms() { 
      return isSms;
  } 
  public void setIsSms(Integer isSms) { 
      this.isSms = isSms;
  } 
  public String getSmsId() { 
      return smsId;
  } 
  public void setSmsId(String smsId) { 
      this.smsId = smsId;
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
