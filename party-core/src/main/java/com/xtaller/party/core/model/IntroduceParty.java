package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/10/08
 */
@SuppressWarnings("serial")
@TableName(value = "introduce_party")
public class IntroduceParty extends Model<IntroduceParty> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private String introduceNumber;
  private String inDuty;
  private String outDuty;
  private Integer introduceJoinTime;
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
  public String getIntroduceNumber() { 
      return introduceNumber;
  } 
  public void setIntroduceNumber(String introduceNumber) { 
      this.introduceNumber = introduceNumber;
  } 
  public String getInDuty() { 
      return inDuty;
  } 
  public void setInDuty(String inDuty) { 
      this.inDuty = inDuty;
  } 
  public String getOutDuty() { 
      return outDuty;
  } 
  public void setOutDuty(String outDuty) { 
      this.outDuty = outDuty;
  } 
  public Integer getIntroduceJoinTime() { 
      return introduceJoinTime;
  } 
  public void setIntroduceJoinTime(Integer introduceJoinTime) { 
      this.introduceJoinTime = introduceJoinTime;
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
