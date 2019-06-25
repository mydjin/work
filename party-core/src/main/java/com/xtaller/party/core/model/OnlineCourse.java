package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/04
 */
@SuppressWarnings("serial")
@TableName(value = "online_course")
public class OnlineCourse extends Model<OnlineCourse> {
  private String id = IdKit.getId(this.getClass());
  private String topic;
  private String title;
  private String summary;
  private Integer isRelease;
  private Integer result;
  private Integer status;
  private String opinion;
  private Integer isReview;
  private Integer isEssence;
  private Integer startTime;
  private Integer endTime;
  private Integer property;
  private String pictureId;
  private String range;
  private Integer isNote;
  private Integer isPush;
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
  public String getTopic() { 
      return topic;
  } 
  public void setTopic(String topic) { 
      this.topic = topic;
  } 
  public String getTitle() { 
      return title;
  } 
  public void setTitle(String title) { 
      this.title = title;
  } 
  public String getSummary() { 
      return summary;
  } 
  public void setSummary(String summary) { 
      this.summary = summary;
  } 
  public Integer getIsRelease() { 
      return isRelease;
  } 
  public void setIsRelease(Integer isRelease) { 
      this.isRelease = isRelease;
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
  public Integer getIsReview() { 
      return isReview;
  } 
  public void setIsReview(Integer isReview) { 
      this.isReview = isReview;
  } 
  public Integer getIsEssence() { 
      return isEssence;
  } 
  public void setIsEssence(Integer isEssence) { 
      this.isEssence = isEssence;
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
  public Integer getProperty() { 
      return property;
  } 
  public void setProperty(Integer property) { 
      this.property = property;
  } 
  public String getPictureId() { 
      return pictureId;
  } 
  public void setPictureId(String pictureId) { 
      this.pictureId = pictureId;
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
