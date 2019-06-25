package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2017/12/01
*/
@SuppressWarnings("serial")
@TableName(value = "base_class")
public class BaseClass extends Model<BaseClass> {
  private String id = IdKit.getId(this.getClass());
  private String gradeId;
  private String name;
  private String controller;
  private String controller2;
  private String controller3;
  private String monitor;
  private String quarters;
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
  public String getGradeId() { 
      return gradeId;
  } 
  public void setGradeId(String gradeId) { 
      this.gradeId = gradeId;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getController() { 
      return controller;
  } 
  public void setController(String controller) { 
      this.controller = controller;
  } 
  public String getController2() { 
      return controller2;
  } 
  public void setController2(String controller2) { 
      this.controller2 = controller2;
  } 
  public String getController3() { 
      return controller3;
  } 
  public void setController3(String controller3) { 
      this.controller3 = controller3;
  } 
  public String getMonitor() { 
      return monitor;
  } 
  public void setMonitor(String monitor) { 
      this.monitor = monitor;
  } 
  public String getQuarters() { 
      return quarters;
  } 
  public void setQuarters(String quarters) { 
      this.quarters = quarters;
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
