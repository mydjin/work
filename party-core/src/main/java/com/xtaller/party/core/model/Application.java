package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/10/23
 */
@SuppressWarnings("serial")
@TableName(value = "application")
public class Application extends Model<Application> {
  private String id = IdKit.getId(this.getClass());
  private String fileURL;
  private String proposerNumber;
  private String lastFileURL;
  private Integer referTime;
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
  public String getProposerNumber() { 
      return proposerNumber;
  } 
  public void setProposerNumber(String proposerNumber) { 
      this.proposerNumber = proposerNumber;
  } 
  public String getLastFileURL() { 
      return lastFileURL;
  } 
  public void setLastFileURL(String lastFileURL) { 
      this.lastFileURL = lastFileURL;
  } 
  public Integer getReferTime() { 
      return referTime;
  } 
  public void setReferTime(Integer referTime) { 
      this.referTime = referTime;
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
