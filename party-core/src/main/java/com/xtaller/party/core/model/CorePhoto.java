package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2018/01/08
*/
@SuppressWarnings("serial")
@TableName(value = "core_photo")
public class CorePhoto extends Model<CorePhoto> {
  private String id = IdKit.getId(this.getClass());
  private String typeId;
  private String image;
  private String title;
  private String description;
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
  public String getTypeId() { 
      return typeId;
  } 
  public void setTypeId(String typeId) { 
      this.typeId = typeId;
  } 
  public String getImage() { 
      return image;
  } 
  public void setImage(String image) { 
      this.image = image;
  } 
  public String getTitle() { 
      return title;
  } 
  public void setTitle(String title) { 
      this.title = title;
  } 
  public String getDescription() { 
      return description;
  } 
  public void setDescription(String description) { 
      this.description = description;
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
