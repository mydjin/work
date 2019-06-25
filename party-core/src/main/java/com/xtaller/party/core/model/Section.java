package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/08/29
 */
@SuppressWarnings("serial")
@TableName(value = "section")
public class Section extends Model<Section> {
  private String id = IdKit.getId(this.getClass());
  private String chapterId;
  private String name;
  private String code;
  private Integer sort;
  private Integer type;
  private String content;
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
  public String getChapterId() { 
      return chapterId;
  } 
  public void setChapterId(String chapterId) { 
      this.chapterId = chapterId;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getCode() { 
      return code;
  } 
  public void setCode(String code) { 
      this.code = code;
  } 
  public Integer getSort() { 
      return sort;
  } 
  public void setSort(Integer sort) { 
      this.sort = sort;
  } 
  public Integer getType() { 
      return type;
  } 
  public void setType(Integer type) { 
      this.type = type;
  } 
  public String getContent() { 
      return content;
  } 
  public void setContent(String content) { 
      this.content = content;
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
