package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/03
 */
@SuppressWarnings("serial")
@TableName(value = "video")
public class Video extends Model<Video> {
  private String id = IdKit.getId(this.getClass());
  private String sectionId;
  private String name;
  private String url;
  private String path;
  private String describe;
  private String code;
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
  public String getSectionId() { 
      return sectionId;
  } 
  public void setSectionId(String sectionId) { 
      this.sectionId = sectionId;
  } 
  public String getName() { 
      return name;
  } 
  public void setName(String name) { 
      this.name = name;
  } 
  public String getUrl() { 
      return url;
  } 
  public void setUrl(String url) { 
      this.url = url;
  } 
  public String getPath() { 
      return path;
  } 
  public void setPath(String path) { 
      this.path = path;
  } 
  public String getDescribe() { 
      return describe;
  } 
  public void setDescribe(String describe) { 
      this.describe = describe;
  } 
  public String getCode() { 
      return code;
  } 
  public void setCode(String code) { 
      this.code = code;
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
