package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/06
 */
@SuppressWarnings("serial")
@TableName(value = "online_course_user")
public class OnlineCourseUser extends Model<OnlineCourseUser> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private String courseId;
  private String score;
  private Integer stage;
  private Integer isFinish;
  private  Float  learnPercent;
  private String note;
  private Integer lastLearnTime;
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
  public String getCourseId() { 
      return courseId;
  } 
  public void setCourseId(String courseId) { 
      this.courseId = courseId;
  } 
  public String getScore() { 
      return score;
  } 
  public void setScore(String score) { 
      this.score = score;
  } 
  public Integer getStage() { 
      return stage;
  } 
  public void setStage(Integer stage) { 
      this.stage = stage;
  } 
  public Integer getIsFinish() { 
      return isFinish;
  } 
  public void setIsFinish(Integer isFinish) { 
      this.isFinish = isFinish;
  } 
  public  Float  getLearnPercent() {
      return learnPercent;
  } 
  public void setLearnPercent( Float  learnPercent) {
      this.learnPercent = learnPercent;
  } 
  public String getNote() { 
      return note;
  } 
  public void setNote(String note) { 
      this.note = note;
  } 
  public Integer getLastLearnTime() { 
      return lastLearnTime;
  } 
  public void setLastLearnTime(Integer lastLearnTime) { 
      this.lastLearnTime = lastLearnTime;
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
