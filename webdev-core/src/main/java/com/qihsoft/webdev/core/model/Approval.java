package com.qihsoft.webdev.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qihsoft.webdev.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Qihsoft on 2018/10/17
 */
@SuppressWarnings("serial")
@TableName(value = "approval")
public class Approval extends Model<Approval> {
  private String id = IdKit.getId(this.getClass());
  private String approvalId;
  private Integer stage;
  private String number;
  private Integer status;
  private Integer isConfirm;
  private Integer result;
  private String opinion;
  private String approverNumber;
  private Integer approvalType;
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
  public String getApprovalId() { 
      return approvalId;
  } 
  public void setApprovalId(String approvalId) { 
      this.approvalId = approvalId;
  } 
  public Integer getStage() { 
      return stage;
  } 
  public void setStage(Integer stage) { 
      this.stage = stage;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  }

    public Integer getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }

    public Integer getStatus() {
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public Integer getResult() { 
      return result;
  } 
  public void setResult(Integer result) { 
      this.result = result;
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
  public Integer getApprovalType() { 
      return approvalType;
  } 
  public void setApprovalType(Integer approvalType) { 
      this.approvalType = approvalType;
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
