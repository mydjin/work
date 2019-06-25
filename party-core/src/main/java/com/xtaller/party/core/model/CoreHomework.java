package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/03
*/
@SuppressWarnings("serial")
@TableName(value = "core_homework")
public class CoreHomework extends Model<CoreHomework> {
  private String id = IdKit.getId(this.getClass());
  private String semesterId;
  private String classId;
  private String gradeId;
  private String courseId;
  private String teacherId;
  private String title;
  private String content;
  private Integer browse;
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
  public String getSemesterId() { 
      return semesterId;
  } 
  public void setSemesterId(String semesterId) { 
      this.semesterId = semesterId;
  } 
  public String getClassId() { 
      return classId;
  } 
  public void setClassId(String classId) { 
      this.classId = classId;
  } 
  public String getGradeId() { 
      return gradeId;
  } 
  public void setGradeId(String gradeId) { 
      this.gradeId = gradeId;
  } 
  public String getCourseId() { 
      return courseId;
  } 
  public void setCourseId(String courseId) { 
      this.courseId = courseId;
  } 
  public String getTeacherId() { 
      return teacherId;
  } 
  public void setTeacherId(String teacherId) { 
      this.teacherId = teacherId;
  } 
  public String getTitle() { 
      return title;
  } 
  public void setTitle(String title) { 
      this.title = title;
  } 
  public String getContent() { 
      return content;
  } 
  public void setContent(String content) { 
      this.content = content;
  }

    public Integer getBrowse() {
        return browse;
    }

    public void setBrowse(Integer browse) {
        this.browse = browse;
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
