package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/19
*/
@SuppressWarnings("serial")
@TableName(value = "data_article")
public class DataArticle extends Model<DataArticle> {
  private String id = IdKit.getId(this.getClass());
  private String title;
  private String video;
  private String content;
  private String introduction;
  private Integer type;
  private String gradeId;
  private String classId;
  private Integer browse;
  private Integer top;
  private Integer zan;
  private Integer comment;
  private Integer status;
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
  public String getTitle() { 
      return title;
  } 
  public void setTitle(String title) { 
      this.title = title;
  } 
  public String getVideo() { 
      return video;
  } 
  public void setVideo(String video) { 
      this.video = video;
  } 
  public String getContent() { 
      return content;
  } 
  public void setContent(String content) { 
      this.content = content;
  } 
  public String getIntroduction() { 
      return introduction;
  } 
  public void setIntroduction(String introduction) { 
      this.introduction = introduction;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public String getGradeId() { 
      return gradeId;
  } 
  public void setGradeId(String gradeId) { 
      this.gradeId = gradeId;
  } 
  public String getClassId() { 
      return classId;
  } 
  public void setClassId(String classId) { 
      this.classId = classId;
  } 
  public Integer getBrowse() { 
      return browse;
  } 
  public void setBrowse(Integer browse) { 
      this.browse = browse;
  } 
  public Integer getTop() { 
      return top;
  } 
  public void setTop(Integer top) { 
      this.top = top;
  } 
  public Integer getZan() { 
      return zan;
  } 
  public void setZan(Integer zan) { 
      this.zan = zan;
  } 
  public Integer getComment() { 
      return comment;
  } 
  public void setComment(Integer comment) { 
      this.comment = comment;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
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
