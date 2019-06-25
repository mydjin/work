package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/05/28
 */
@SuppressWarnings("serial")
@TableName(value = "exam_candidate")
public class ExamCandidate extends Model<ExamCandidate> {
  private String id = IdKit.getId(this.getClass());
  private String number;
  private String examId;
  private String roomId;
  private String score;
  private Integer status;
  private String remark;
  private String ipAddr;
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
  public String getExamId() { 
      return examId;
  } 
  public void setExamId(String examId) { 
      this.examId = examId;
  } 
  public String getRoomId() { 
      return roomId;
  } 
  public void setRoomId(String roomId) { 
      this.roomId = roomId;
  } 
  public String getScore() { 
      return score;
  } 
  public void setScore(String score) { 
      this.score = score;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public String getRemark() { 
      return remark;
  } 
  public void setRemark(String remark) { 
      this.remark = remark;
  } 
  public String getIpAddr() { 
      return ipAddr;
  } 
  public void setIpAddr(String ipAddr) { 
      this.ipAddr = ipAddr;
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
