package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/05/31
 */
@SuppressWarnings("serial")
@TableName(value = "exam")
public class Exam extends Model<Exam> {
  private String id = IdKit.getId(this.getClass());
  private String name;
  private Integer status;
  private Integer releaseStatus;
  private Integer type;
  private Integer way;
  private String score;
  private String trueOrFalse;
  private String single;
  private String multiple;
  private String gap;
  private Integer startTime;
  private Integer endTime;
  private Integer duration;
  private String range;
  private Integer isNote;
  private Integer isPush;
  private Integer isMake;
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
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public Integer getReleaseStatus() { 
      return releaseStatus;
  } 
  public void setReleaseStatus(Integer releaseStatus) { 
      this.releaseStatus = releaseStatus;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public Integer getWay() { 
      return way;
  } 
  public void setWay(Integer way) { 
      this.way = way;
  } 
  public String getScore() { 
      return score;
  } 
  public void setScore(String score) { 
      this.score = score;
  } 
  public String getTrueOrFalse() { 
      return trueOrFalse;
  } 
  public void setTrueOrFalse(String trueOrFalse) { 
      this.trueOrFalse = trueOrFalse;
  } 
  public String getSingle() { 
      return single;
  } 
  public void setSingle(String single) { 
      this.single = single;
  } 
  public String getMultiple() { 
      return multiple;
  } 
  public void setMultiple(String multiple) { 
      this.multiple = multiple;
  } 
  public String getGap() { 
      return gap;
  } 
  public void setGap(String gap) { 
      this.gap = gap;
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
  public Integer getDuration() { 
      return duration;
  } 
  public void setDuration(Integer duration) { 
      this.duration = duration;
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
  public Integer getIsMake() { 
      return isMake;
  } 
  public void setIsMake(Integer isMake) { 
      this.isMake = isMake;
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
