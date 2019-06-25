package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/06/05
 */
@SuppressWarnings("serial")
@TableName(value = "exam_room")
public class ExamRoom extends Model<ExamRoom> {
  private String id = IdKit.getId(this.getClass());
  private String examId;
  private String name;
  private String monitor;
  private Integer isUnseal;
  private String unsealCode;
  private Integer status;
  private String remark;
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
  public String getExamId() { 
      return examId;
  } 
  public void setExamId(String examId) { 
      this.examId = examId;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getMonitor() { 
      return monitor;
  } 
  public void setMonitor(String monitor) { 
      this.monitor = monitor;
  } 
  public Integer getIsUnseal() { 
      return isUnseal;
  } 
  public void setIsUnseal(Integer isUnseal) { 
      this.isUnseal = isUnseal;
  } 
  public String getUnsealCode() { 
      return unsealCode;
  } 
  public void setUnsealCode(String unsealCode) { 
      this.unsealCode = unsealCode;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public String getRemark() { 
      return remark;
  } 
  public void setRemark(String remark) { 
      this.remark = remark;
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
