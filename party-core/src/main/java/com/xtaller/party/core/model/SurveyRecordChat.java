package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/03/09
 */
@SuppressWarnings("serial")
@TableName(value = "survey_record_chat")
public class SurveyRecordChat extends Model<SurveyRecordChat> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private Integer surveyTime;
  private String surveyCondition;
  private Integer approvalStatus;
  private String approvalOpinion;
  private Integer approvalResult;
  private String approverNumber;
  private Integer type;
  private Integer stage;
  private Integer count;
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
  public Integer getSurveyTime() { 
      return surveyTime;
  } 
  public void setSurveyTime(Integer surveyTime) { 
      this.surveyTime = surveyTime;
  }
  public String getSurveyCondition() { 
      return surveyCondition;
  } 
  public void setSurveyCondition(String surveyCondition) { 
      this.surveyCondition = surveyCondition;
  } 
  public Integer getApprovalStatus() { 
      return approvalStatus;
  } 
  public void setApprovalStatus(Integer approvalStatus) { 
      this.approvalStatus = approvalStatus;
  } 
  public String getApprovalOpinion() { 
      return approvalOpinion;
  } 
  public void setApprovalOpinion(String approvalOpinion) { 
      this.approvalOpinion = approvalOpinion;
  } 
  public Integer getApprovalResult() { 
      return approvalResult;
  } 
  public void setApprovalResult(Integer approvalResult) { 
      this.approvalResult = approvalResult;
  } 
  public String getApproverNumber() { 
      return approverNumber;
  } 
  public void setApproverNumber(String approverNumber) { 
      this.approverNumber = approverNumber;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public Integer getStage() { 
      return stage;
  } 
  public void setStage(Integer stage) { 
      this.stage = stage;
  } 
  public Integer getCount() { 
      return count;
  } 
  public void setCount(Integer count) { 
      this.count = count;
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
