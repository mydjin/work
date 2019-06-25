package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2017/12/16
*/
@SuppressWarnings("serial")
@TableName(value = "core_sign")
public class CoreSign extends Model<CoreSign> {
  private String id = IdKit.getId(this.getClass());
  private String classId;
  private String studentId;
  private String teacherId;
  private Integer userType;//用户类型 1学生 2老师
    private String cardNo;//卡号
  private Long signDate;
  private String sn;
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
  public String getTeacherId() { 
      return teacherId;
  } 
  public void setTeacherId(String teacherId) { 
      this.teacherId = teacherId;
  }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getSignDate() {
        return signDate;
    }

    public void setSignDate(Long signDate) {
        this.signDate = signDate;
    }

    public String getSn() {
      return sn;
  }
  public void setSn(String sn) {
      this.sn = sn;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
  protected Serializable pkVal() {
      return id;
  }
}
