package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/03
*/
@SuppressWarnings("serial")
@TableName(value = "core_score")
public class CoreScore extends Model<CoreScore> {
  private String id = IdKit.getId(this.getClass());
  private String semesterId;
  private String classId;
  private String gradeId;
  private String studentId;
  private String courseId;
  private Integer score;
  private String scoreLevel;
  private String examTypeId;
  private String teacherId;
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
  public String getStudentId() { 
      return studentId;
  } 
  public void setStudentId(String studentId) { 
      this.studentId = studentId;
  } 
  public String getCourseId() { 
      return courseId;
  } 
  public void setCourseId(String courseId) { 
      this.courseId = courseId;
  } 
  public Integer getScore() { 
      return score;
  } 
  public void setScore(Integer score) { 
      this.score = score;
  } 
  public String getScoreLevel() { 
      return scoreLevel;
  } 
  public void setScoreLevel(String scoreLevel) { 
      this.scoreLevel = scoreLevel;
  } 
  public String getExamTypeId() { 
      return examTypeId;
  } 
  public void setExamTypeId(String examTypeId) { 
      this.examTypeId = examTypeId;
  } 
  public String getTeacherId() { 
      return teacherId;
  } 
  public void setTeacherId(String teacherId) { 
      this.teacherId = teacherId;
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
