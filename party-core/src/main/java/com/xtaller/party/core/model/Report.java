package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/11/11
 */
@SuppressWarnings("serial")
@TableName(value = "report")
public class Report extends Model<Report> {
  private String id = IdKit.getId(this.getClass());
  private String fileURL;
  private String lastFileURL;
//  private Integer status;
//  private String opinion;
//  private Integer result;
  private String proposerNumber;
//  private String verifierNumber;
  private Integer type;
  private Integer referTime;
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
  public String getFileURL() { 
      return fileURL;
  } 
  public void setFileURL(String fileURL) { 
      this.fileURL = fileURL;
  } 
  public String getLastFileURL() { 
      return lastFileURL;
  } 
  public void setLastFileURL(String lastFileURL) { 
      this.lastFileURL = lastFileURL;
  } 
//  public Integer getStatus() {
//      return status;
//  }
//  public void setStatus(Integer status) {
//      this.status = status;
//  }
//  public String getOpinion() {
//      return opinion;
//  }
//  public void setOpinion(String opinion) {
//      this.opinion = opinion;
//  }
//  public Integer getResult() {
//      return result;
//  }
//  public void setResult(Integer result) {
//      this.result = result;
//  }
  public String getProposerNumber() { 
      return proposerNumber;
  } 
  public void setProposerNumber(String proposerNumber) { 
      this.proposerNumber = proposerNumber;
  } 
//  public String getVerifierNumber() {
//      return verifierNumber;
//  }
//  public void setVerifierNumber(String verifierNumber) {
//      this.verifierNumber = verifierNumber;
//  }
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public Integer getReferTime() { 
      return referTime;
  } 
  public void setReferTime(Integer referTime) { 
      this.referTime = referTime;
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
