package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/08/20
 */
@SuppressWarnings("serial")
@TableName(value = "vacate")
public class Vacate extends Model<Vacate> {
  private String id = IdKit.getId(this.getClass());
  private String scheduleId;
  private Integer type;
  private String number;
  private String reason;
  private Integer result;
  private Integer status;
  private String opinion;
  private String approverNumber;
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
  public String getScheduleId() { 
      return scheduleId;
  } 
  public void setScheduleId(String scheduleId) { 
      this.scheduleId = scheduleId;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  } 
  public String getReason() { 
      return reason;
  } 
  public void setReason(String reason) { 
      this.reason = reason;
  } 
  public Integer getResult() { 
      return result;
  } 
  public void setResult(Integer result) { 
      this.result = result;
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
  public String getApproverNumber() { 
      return approverNumber;
  } 
  public void setApproverNumber(String approverNumber) { 
      this.approverNumber = approverNumber;
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
