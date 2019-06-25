package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/10/08
 */
@SuppressWarnings("serial")
@TableName(value = "link_user")
public class LinkUser extends Model<LinkUser> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private String linkNumber;
  private String inDuty;
  private String outDuty;
  private Integer linkJoinTime;
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
  public String getLinkNumber() { 
      return linkNumber;
  } 
  public void setLinkNumber(String linkNumber) { 
      this.linkNumber = linkNumber;
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
  public Integer getLinkJoinTime() { 
      return linkJoinTime;
  } 
  public void setLinkJoinTime(Integer linkJoinTime) { 
      this.linkJoinTime = linkJoinTime;
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
