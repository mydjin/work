package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/03/14
 */
@SuppressWarnings("serial")
@TableName(value = "schedule")
public class Schedule extends Model<Schedule> {
  private String id = IdKit.getId(this.getClass());
  private String summary;
  private String topic;
  private String place;
  private String position;
  private Integer status;
  private String score;
  private Integer type;
  private String speaker;
  private Integer startTime;
  private Integer endTime;
  private String range;
  private Integer isNote;
  private Integer isPush;
  private Integer checkType;
  private String linkmanNumber;
  private String linkmanPhone;
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
  public String getSummary() { 
      return summary;
  } 
  public void setSummary(String summary) { 
      this.summary = summary;
  } 
  public String getTopic() { 
      return topic;
  } 
  public void setTopic(String topic) { 
      this.topic = topic;
  } 
  public String getPlace() { 
      return place;
  } 
  public void setPlace(String place) { 
      this.place = place;
  } 
  public String getPosition() { 
      return position;
  } 
  public void setPosition(String position) { 
      this.position = position;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public String getScore() { 
      return score;
  } 
  public void setScore(String score) { 
      this.score = score;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public String getSpeaker() { 
      return speaker;
  } 
  public void setSpeaker(String speaker) { 
      this.speaker = speaker;
  } 
  public Integer getStartTime() { 
      return startTime;
  } 
  public void setStartTime(Integer startTime) { 
      this.startTime = startTime;
  } 
  public Integer getEndTime() { 
      return endTime;
  } 
  public void setEndTime(Integer endTime) { 
      this.endTime = endTime;
  } 
  public String getRange() { 
      return range;
  } 
  public void setRange(String range) { 
      this.range = range;
  } 
  public Integer getIsNote() { 
      return isNote;
  } 
  public void setIsNote(Integer isNote) { 
      this.isNote = isNote;
  } 
  public Integer getIsPush() { 
      return isPush;
  } 
  public void setIsPush(Integer isPush) { 
      this.isPush = isPush;
  } 
  public Integer getCheckType() { 
      return checkType;
  } 
  public void setCheckType(Integer checkType) { 
      this.checkType = checkType;
  } 
  public String getLinkmanNumber() { 
      return linkmanNumber;
  } 
  public void setLinkmanNumber(String linkmanNumber) { 
      this.linkmanNumber = linkmanNumber;
  } 
  public String getLinkmanPhone() { 
      return linkmanPhone;
  } 
  public void setLinkmanPhone(String linkmanPhone) { 
      this.linkmanPhone = linkmanPhone;
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
