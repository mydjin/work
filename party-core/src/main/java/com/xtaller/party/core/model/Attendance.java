package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Party on 2019/03/14
 */
@SuppressWarnings("serial")
@TableName(value = "attendance")
public class Attendance extends Model<Attendance> {
  private String id = IdKit.getId(this.getClass());
  private Integer attendanceTime;
  private String scheduleId;
  private Integer status;
  private String number;
  private Integer way;
  private Integer type;
  private String position;
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
  public Integer getAttendanceTime() { 
      return attendanceTime;
  } 
  public void setAttendanceTime(Integer attendanceTime) { 
      this.attendanceTime = attendanceTime;
  } 
  public String getScheduleId() { 
      return scheduleId;
  } 
  public void setScheduleId(String scheduleId) { 
      this.scheduleId = scheduleId;
  } 
  public Integer getStatus() { 
      return status;
  } 
  public void setStatus(Integer status) { 
      this.status = status;
  } 
  public String getNumber() { 
      return number;
  } 
  public void setNumber(String number) { 
      this.number = number;
  } 
  public Integer getWay() { 
      return way;
  } 
  public void setWay(Integer way) { 
      this.way = way;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public String getPosition() { 
      return position;
  } 
  public void setPosition(String position) { 
      this.position = position;
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
