package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/06
 */
@SuppressWarnings("serial")
@TableName(value = "chapter_user")
public class ChapterUser extends Model<ChapterUser> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private String chapterCode;
  private String testScore;
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
  public String getChapterCode() { 
      return chapterCode;
  } 
  public void setChapterCode(String chapterCode) { 
      this.chapterCode = chapterCode;
  } 
  public String getTestScore() { 
      return testScore;
  } 
  public void setTestScore(String testScore) { 
      this.testScore = testScore;
  } 
  public Integer getIsFinish() { 
      return isFinish;
  } 
  public void setIsFinish(Integer isFinish) { 
      this.isFinish = isFinish;
  } 
  public Float getLearnPercent() {
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
